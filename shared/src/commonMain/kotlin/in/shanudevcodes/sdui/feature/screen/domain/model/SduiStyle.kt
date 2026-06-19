package `in`.shanudevcodes.sdui.feature.screen.domain.model

/**
 * Domain representation of text styling properties, independent of serialization/network libraries.
 */
data class SduiStyle(
    val fontSize: Int? = null,
    val fontWeight: String? = null,
    val fontStyle: String? = null,
    val color: String? = null,
    val textAlign: String? = null,
    val maxLines: Int? = null,
    val overflow: String? = null
)
