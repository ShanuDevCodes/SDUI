package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.Serializable

/**
 * DTO representing text style attributes parsed from screen JSON.
 */
@Serializable
data class SduiStyleDto(
    val fontSize: Int? = null,
    val fontWeight: String? = null,
    val fontStyle: String? = null,
    val color: String? = null,
    val textAlign: String? = null,
    val letterSpacing: Float? = null,
    val lineHeight: Int? = null,
    val maxLines: Int? = null,
    val overflow: String? = null,
    val textDecoration: String? = null
)
