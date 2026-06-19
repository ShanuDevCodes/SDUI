package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Root DTO for a screen definition received from the backend.
 */
@Serializable
data class SduiScreenDto(
    val screenId: String,
    val schemaVersion: String = "1.0.0",
    val title: String? = null,
    val initialState: Map<String, JsonElement> = emptyMap(),
    val theme: Map<String, String> = emptyMap(),
    val root: SduiComponentDto
)
