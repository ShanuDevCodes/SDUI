package `in`.shanudevcodes.sdui.feature.screen.domain.model

import kotlinx.serialization.json.JsonElement

/**
 * Domain model representing a loaded and validated server-driven UI screen definition.
 */
data class ScreenDefinition(
    val screenId: String,
    val schemaVersion: String,
    val title: String,
    val root: SduiNode,
    val theme: Map<String, String> = emptyMap(),
    val initialState: Map<String, JsonElement> = emptyMap()
)
