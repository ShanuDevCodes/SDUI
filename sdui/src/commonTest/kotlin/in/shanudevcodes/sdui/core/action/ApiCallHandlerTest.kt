package `in`.shanudevcodes.sdui.core.action

import `in`.shanudevcodes.sdui.core.engine.SduiConfig
import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class ApiCallHandlerTest {

    private lateinit var stateHolder: SduiStateHolder

    @BeforeTest
    fun setUp() {
        stateHolder = SduiStateHolder()
        SduiEngine.reset()
    }

    @AfterTest
    fun tearDown() {
        SduiEngine.reset()
    }

    @Test
    fun testHandle_successfulRequest_executesOnSuccess() = runTest {
        stateHolder.setValue("email", JsonPrimitive("test@example.com"))

        var requestPath: String? = null
        var requestMethod: String? = null
        var requestBody: String? = null

        val mockEngine = MockEngine { request ->
            requestPath = request.url.encodedPath
            requestMethod = request.method.value
            requestBody = request.body.toByteArray().decodeToString()
            respond(
                content = """{"status":"ok"}""",
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }

        SduiEngine.initialize(
            config = SduiConfig(baseUrl = "https://api.example.com"),
            httpClientEngine = mockEngine
        )

        var successExecuted = false
        val dispatchAction: suspend (SduiActionDto) -> Unit = { action ->
            if (action.type == "SuccessAction") {
                successExecuted = true
            }
        }

        val handler = ApiCallHandler(
            stateProvider = { stateHolder.state.value },
            dispatchAction = dispatchAction
        )

        val action = SduiActionDto(
            type = "ApiCall",
            endpoint = "/submit",
            method = "POST",
            body = JsonObject(mapOf("userEmail" to JsonPrimitive("{{email}}"))),
            onSuccess = SduiActionDto(type = "SuccessAction"),
            onError = SduiActionDto(type = "ErrorAction")
        )

        handler.handle(action)

        assertTrue(successExecuted)
        assertEquals("/submit", requestPath)
        assertEquals("POST", requestMethod)
        val expectedJson = Json.parseToJsonElement("""{"userEmail":"test@example.com"}""")
        val actualJson = requestBody?.let { Json.parseToJsonElement(it) }
        assertEquals(expectedJson, actualJson)
    }

    @Test
    fun testHandle_failedRequest_executesOnError() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = """{"error":"bad request"}""",
                status = HttpStatusCode.BadRequest,
                headers = headersOf("Content-Type", "application/json")
            )
        }

        SduiEngine.initialize(
            config = SduiConfig(baseUrl = "https://api.example.com"),
            httpClientEngine = mockEngine
        )

        var errorExecuted = false
        val dispatchAction: suspend (SduiActionDto) -> Unit = { action ->
            if (action.type == "ErrorAction") {
                errorExecuted = true
            }
        }

        val handler = ApiCallHandler(
            stateProvider = { stateHolder.state.value },
            dispatchAction = dispatchAction
        )

        val action = SduiActionDto(
            type = "ApiCall",
            endpoint = "/submit",
            method = "POST",
            onSuccess = SduiActionDto(type = "SuccessAction"),
            onError = SduiActionDto(type = "ErrorAction")
        )

        handler.handle(action)

        assertTrue(errorExecuted)
    }

    @Test
    fun testHandle_exceptionThrown_executesOnError() = runTest {
        val mockEngine = MockEngine { _ ->
            throw RuntimeException("Network timeout")
        }

        SduiEngine.initialize(
            config = SduiConfig(baseUrl = "https://api.example.com"),
            httpClientEngine = mockEngine
        )

        var errorExecuted = false
        val dispatchAction: suspend (SduiActionDto) -> Unit = { action ->
            if (action.type == "ErrorAction") {
                errorExecuted = true
            }
        }

        val handler = ApiCallHandler(
            stateProvider = { stateHolder.state.value },
            dispatchAction = dispatchAction
        )

        val action = SduiActionDto(
            type = "ApiCall",
            endpoint = "/submit",
            method = "POST",
            onSuccess = SduiActionDto(type = "SuccessAction"),
            onError = SduiActionDto(type = "ErrorAction")
        )

        handler.handle(action)

        assertTrue(errorExecuted)
    }
}
