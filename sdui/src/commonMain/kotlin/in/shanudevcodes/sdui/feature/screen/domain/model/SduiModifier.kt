package `in`.shanudevcodes.sdui.feature.screen.domain.model

/**
 * Domain representation of modifiers independent of serialization.
 */
public data class SduiModifier(
    public val type: String,
    public val valueStr: String? = null
)
