package `in`.shanudevcodes.sdui.core.registry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.components.BoxRenderer
import `in`.shanudevcodes.sdui.core.components.ButtonRenderer
import `in`.shanudevcodes.sdui.core.components.CardRenderer
import `in`.shanudevcodes.sdui.core.components.CheckboxRenderer
import `in`.shanudevcodes.sdui.core.components.ColumnRenderer
import `in`.shanudevcodes.sdui.core.components.DividerRenderer
import `in`.shanudevcodes.sdui.core.components.DropdownMenuRenderer
import `in`.shanudevcodes.sdui.core.components.IconRenderer
import `in`.shanudevcodes.sdui.core.components.ImageRenderer
import `in`.shanudevcodes.sdui.core.components.RadioButtonRenderer
import `in`.shanudevcodes.sdui.core.components.RowRenderer
import `in`.shanudevcodes.sdui.core.components.SliderRenderer
import `in`.shanudevcodes.sdui.core.components.SpacerRenderer
import `in`.shanudevcodes.sdui.core.components.SurfaceRenderer
import `in`.shanudevcodes.sdui.core.components.SwitchRenderer
import `in`.shanudevcodes.sdui.core.components.TextFieldRenderer
import `in`.shanudevcodes.sdui.core.components.TextRenderer
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
        // Pre-register implementations for core built-in layouts/display types.
        register("Column") { component, modifier, stateHolder ->
            ColumnRenderer(component, modifier, stateHolder)
        }
        register("Row") { component, modifier, stateHolder ->
            RowRenderer(component, modifier, stateHolder)
        }
        register("Box") { component, modifier, stateHolder ->
            BoxRenderer(component, modifier, stateHolder)
        }
        register("Spacer") { component, modifier, stateHolder ->
            SpacerRenderer(component, modifier, stateHolder)
        }
        register("Text") { component, modifier, stateHolder ->
            TextRenderer(component, modifier, stateHolder)
        }
        register("Image") { component, modifier, stateHolder ->
            ImageRenderer(component, modifier, stateHolder)
        }
        register("Icon") { component, modifier, stateHolder ->
            IconRenderer(component, modifier, stateHolder)
        }
        register("Divider") { component, modifier, stateHolder ->
            DividerRenderer(component, modifier, stateHolder)
        }
        register("Card") { component, modifier, stateHolder ->
            CardRenderer(component, modifier, stateHolder)
        }
        register("Surface") { component, modifier, stateHolder ->
            SurfaceRenderer(component, modifier, stateHolder)
        }
        register("Button") { component, modifier, stateHolder ->
            ButtonRenderer(component, modifier, stateHolder)
        }
        register("TextField") { component, modifier, stateHolder ->
            TextFieldRenderer(component, modifier, stateHolder)
        }
        register("Switch") { component, modifier, stateHolder ->
            SwitchRenderer(component, modifier, stateHolder)
        }
        register("Checkbox") { component, modifier, stateHolder ->
            CheckboxRenderer(component, modifier, stateHolder)
        }
        register("RadioButton") { component, modifier, stateHolder ->
            RadioButtonRenderer(component, modifier, stateHolder)
        }
        register("Slider") { component, modifier, stateHolder ->
            SliderRenderer(component, modifier, stateHolder)
        }
        register("DropdownMenu") { component, modifier, stateHolder ->
            DropdownMenuRenderer(component, modifier, stateHolder)
        }
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
        register("Column") { component, modifier, stateHolder ->
            ColumnRenderer(component, modifier, stateHolder)
        }
        register("Row") { component, modifier, stateHolder ->
            RowRenderer(component, modifier, stateHolder)
        }
        register("Box") { component, modifier, stateHolder ->
            BoxRenderer(component, modifier, stateHolder)
        }
        register("Spacer") { component, modifier, stateHolder ->
            SpacerRenderer(component, modifier, stateHolder)
        }
        register("Text") { component, modifier, stateHolder ->
            TextRenderer(component, modifier, stateHolder)
        }
        register("Image") { component, modifier, stateHolder ->
            ImageRenderer(component, modifier, stateHolder)
        }
        register("Icon") { component, modifier, stateHolder ->
            IconRenderer(component, modifier, stateHolder)
        }
        register("Divider") { component, modifier, stateHolder ->
            DividerRenderer(component, modifier, stateHolder)
        }
        register("Card") { component, modifier, stateHolder ->
            CardRenderer(component, modifier, stateHolder)
        }
        register("Surface") { component, modifier, stateHolder ->
            SurfaceRenderer(component, modifier, stateHolder)
        }
        register("Button") { component, modifier, stateHolder ->
            ButtonRenderer(component, modifier, stateHolder)
        }
        register("TextField") { component, modifier, stateHolder ->
            TextFieldRenderer(component, modifier, stateHolder)
        }
        register("Switch") { component, modifier, stateHolder ->
            SwitchRenderer(component, modifier, stateHolder)
        }
        register("Checkbox") { component, modifier, stateHolder ->
            CheckboxRenderer(component, modifier, stateHolder)
        }
        register("RadioButton") { component, modifier, stateHolder ->
            RadioButtonRenderer(component, modifier, stateHolder)
        }
        register("Slider") { component, modifier, stateHolder ->
            SliderRenderer(component, modifier, stateHolder)
        }
        register("DropdownMenu") { component, modifier, stateHolder ->
            DropdownMenuRenderer(component, modifier, stateHolder)
        }
    }
}
