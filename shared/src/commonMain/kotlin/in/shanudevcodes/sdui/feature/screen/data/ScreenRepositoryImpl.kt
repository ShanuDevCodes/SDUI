package `in`.shanudevcodes.sdui.feature.screen.data

import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.core.schema.SduiScreenDto
import `in`.shanudevcodes.sdui.feature.screen.data.mapper.ScreenMapper
import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition
import `in`.shanudevcodes.sdui.feature.screen.domain.repository.ScreenRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Implementation of ScreenRepository that fetches screen definition payloads from the server via Ktor.
 */
class ScreenRepositoryImpl : ScreenRepository {

    override suspend fun getScreen(
        screenId: String,
        params: Map<String, String>
    ): Result<ScreenDefinition> = runCatching {
        val client = SduiEngine.getHttpClient()
        val response = client.get("sdui/screens/$screenId.json") {
            params.forEach { (key, value) ->
                parameter(key, value)
            }
        }
        val screenDto: SduiScreenDto = response.body()
        ScreenMapper.map(screenDto)
    }
}
