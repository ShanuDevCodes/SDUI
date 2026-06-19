package `in`.shanudevcodes.sdui.core.registry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Type signature for a component rendering block in Compose.
 */
typealias ComponentRenderer = @Composable (
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) -> Unit

/**
 * Registry mapping component types (e.g. "Text", "Button") to their Compose Composable renderers.
 */
object ComponentRegistry {
    private val renderers = mutableMapOf<String, ComponentRenderer>()

    init {
        // Pre-register stubs for core built-in layouts/display types.
        // These will be fully replaced with actual Compose renderers in SDUI-008 and SDUI-009.
        register("Column") { _, _, _ -> }
        register("Row") { _, _, _ -> }
        register("Box") { _, _, _ -> }
        register("Spacer") { _, _, _ -> }
        register("Text") { _, _, _ -> }
        register("Button") { _, _, _ -> }
        register("Image") { _, _, _ -> }
    }

    /**
     * Registers a new or custom component renderer.
     */
    fun register(type: String, renderer: ComponentRenderer) {
        renderers[type] = renderer
    }

    /**
     * Resolves a registered component renderer by type name, returning null if unknown (graceful fallback).
     */
    fun resolve(type: String): ComponentRenderer? {
        return renderers[type]
    }

    /**
     * Resets the registry back to its default pre-registered built-ins (useful for tests).
     */
    fun reset() {
        renderers.clear()
        register("Column") { _, _, _ -> }
        register("Row") { _, _, _ -> }
        register("Box") { _, _, _ -> }
        register("Spacer") { _, _, _ -> }
        register("Text") { _, _, _ -> }
        register("Button") { _, _, _ -> }
        register("Image") { _, _, _ -> }
    }
}
