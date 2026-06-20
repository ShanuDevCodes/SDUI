package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * DTO representing a UI modifier (spacing, size, clip, border, clickable, etc.).
 */
@Serializable
public data class SduiModifierDto(
    val type: String,
    val value: JsonElement? = null
)
