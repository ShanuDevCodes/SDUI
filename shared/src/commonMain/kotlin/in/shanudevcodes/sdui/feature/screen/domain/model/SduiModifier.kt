package `in`.shanudevcodes.sdui.feature.screen.domain.model

/**
 * Domain representation of modifiers independent of serialization.
 */
data class SduiModifier(
    val type: String,
    val valueStr: String? = null
)
