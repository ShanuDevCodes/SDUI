package `in`.shanudevcodes.sdui.core.action

import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.state.TemplateResolver
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Handler for the `ApiCall` action type. Makes HTTP requests and dispatches success/error follow-up actions.
 */
class ApiCallHandler(
    private val stateProvider: () -> Map<String, JsonElement>,
    private val dispatchAction: suspend (SduiActionDto) -> Unit
) {
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Handles the ApiCall action.
     */
    suspend fun handle(action: SduiActionDto) {
        val endpoint = action.endpoint ?: return
        val methodStr = action.method ?: "POST"
        val body = action.body

        val state = stateProvider()
        val resolvedBody = body?.let { resolveJsonElement(it, state) }

        try {
            val client = SduiEngine.getHttpClient()
            val response: HttpResponse = client.request(endpoint) {
                method = HttpMethod.parse(methodStr)
                if (resolvedBody != null) {
                    contentType(ContentType.Application.Json)
                    setBody(resolvedBody)
                }
            }

            if (response.status.isSuccess()) {
                action.onSuccess?.let { dispatchAction(it) }
            } else {
                action.onError?.let { dispatchAction(it) }
            }
        } catch (e: Exception) {
            action.onError?.let { dispatchAction(it) }
        }
    }

    private fun resolveJsonElement(element: JsonElement, state: Map<String, JsonElement>): JsonElement {
        val jsonString = element.toString()
        val resolvedString = TemplateResolver.resolve(jsonString, state)
        return try {
            json.parseToJsonElement(resolvedString)
        } catch (e: Exception) {
            element
        }
    }
}
