package `in`.shanudevcodes.sdui.feature.screen.domain.model

/**
 * Domain model representing a loaded and validated server-driven UI screen definition.
 */
data class ScreenDefinition(
    val screenId: String,
    val schemaVersion: String,
    val title: String,
    val root: SduiNode,
    val theme: Map<String, String> = emptyMap()
)
