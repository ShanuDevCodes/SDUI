package `in`.shanudevcodes.sdui.feature.screen.domain.model

/**
 * Domain representation of text styling properties, independent of serialization/network libraries.
 */
public data class SduiStyle(
    public val fontSize: Int? = null,
    public val fontWeight: String? = null,
    public val fontStyle: String? = null,
    public val color: String? = null,
    public val textAlign: String? = null,
    public val maxLines: Int? = null,
    public val overflow: String? = null,
    public val letterSpacing: Float? = null,
    public val lineHeight: Int? = null,
    public val textDecoration: String? = null
)
