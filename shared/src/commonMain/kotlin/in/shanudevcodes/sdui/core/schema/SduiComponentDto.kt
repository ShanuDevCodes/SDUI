package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * DTO representing an individual UI node in the SDUI component tree.
 */
@Serializable
data class SduiComponentDto(
    val type: String,
    val props: Map<String, JsonElement> = emptyMap(),
    val modifiers: List<SduiModifierDto> = emptyList(),
    val children: List<SduiComponentDto> = emptyList(),
    val actions: Map<String, SduiActionDto> = emptyMap()
)
