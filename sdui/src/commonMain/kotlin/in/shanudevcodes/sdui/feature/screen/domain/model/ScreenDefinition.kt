package `in`.shanudevcodes.sdui.feature.screen.domain.model

import kotlinx.serialization.json.JsonElement

/**
 * Domain model representing a loaded and validated server-driven UI screen definition.
 */
public data class ScreenDefinition(
    public val screenId: String,
    public val schemaVersion: String,
    public val title: String,
    public val root: SduiNode,
    public val theme: Map<String, String> = emptyMap(),
    public val initialState: Map<String, JsonElement> = emptyMap()
)
