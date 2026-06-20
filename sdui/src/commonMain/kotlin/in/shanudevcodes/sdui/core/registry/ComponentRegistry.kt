package `in`.shanudevcodes.sdui.core.registry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.components.BoxRenderer
import `in`.shanudevcodes.sdui.core.components.ButtonRenderer
import `in`.shanudevcodes.sdui.core.components.CardRenderer
import `in`.shanudevcodes.sdui.core.components.CheckboxRenderer
import `in`.shanudevcodes.sdui.core.components.CircularProgressRenderer
import `in`.shanudevcodes.sdui.core.components.ColumnRenderer
import `in`.shanudevcodes.sdui.core.components.ConditionalRenderer
import `in`.shanudevcodes.sdui.core.components.DividerRenderer
import `in`.shanudevcodes.sdui.core.components.DropdownMenuRenderer
import `in`.shanudevcodes.sdui.core.components.IconButtonRenderer
import `in`.shanudevcodes.sdui.core.components.IconRenderer
import `in`.shanudevcodes.sdui.core.components.ImageRenderer
import `in`.shanudevcodes.sdui.core.components.LazyColumnRenderer
import `in`.shanudevcodes.sdui.core.components.LazyGridRenderer
import `in`.shanudevcodes.sdui.core.components.LazyRowRenderer
import `in`.shanudevcodes.sdui.core.components.LinearProgressRenderer
import `in`.shanudevcodes.sdui.core.components.RadioButtonRenderer
import `in`.shanudevcodes.sdui.core.components.RowRenderer
import `in`.shanudevcodes.sdui.core.components.ScaffoldRenderer
import `in`.shanudevcodes.sdui.core.components.SliderRenderer
import `in`.shanudevcodes.sdui.core.components.SpacerRenderer
import `in`.shanudevcodes.sdui.core.components.SurfaceRenderer
import `in`.shanudevcodes.sdui.core.components.SwitchRenderer
import `in`.shanudevcodes.sdui.core.components.TextFieldRenderer
import `in`.shanudevcodes.sdui.core.components.TextRenderer
import `in`.shanudevcodes.sdui.core.components.VisibleRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/** Type signature for a component rendering block. */
public typealias ComponentRenderer = @Composable (
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) -> Unit

/** Registry mapping component type strings to their Compose renderers. */
public object ComponentRegistry {
    private val renderers = mutableMapOf<String, ComponentRenderer>()

    init { registerBuiltIns() }

    private fun registerBuiltIns() {
        register("Column") { c, m, s -> ColumnRenderer(c, m, s) }
        register("Row") { c, m, s -> RowRenderer(c, m, s) }
        register("Box") { c, m, s -> BoxRenderer(c, m, s) }
        register("Spacer") { c, m, s -> SpacerRenderer(c, m, s) }
        register("Text") { c, m, s -> TextRenderer(c, m, s) }
        register("Image") { c, m, s -> ImageRenderer(c, m, s) }
        register("Icon") { c, m, s -> IconRenderer(c, m, s) }
        register("Divider") { c, m, s -> DividerRenderer(c, m, s) }
        register("Card") { c, m, s -> CardRenderer(c, m, s) }
        register("Surface") { c, m, s -> SurfaceRenderer(c, m, s) }
        register("Button") { c, m, s -> ButtonRenderer(c, m, s) }
        register("TextField") { c, m, s -> TextFieldRenderer(c, m, s) }
        register("Switch") { c, m, s -> SwitchRenderer(c, m, s) }
        register("Checkbox") { c, m, s -> CheckboxRenderer(c, m, s) }
        register("RadioButton") { c, m, s -> RadioButtonRenderer(c, m, s) }
        register("Slider") { c, m, s -> SliderRenderer(c, m, s) }
        register("DropdownMenu") { c, m, s -> DropdownMenuRenderer(c, m, s) }
        register("LazyColumn") { c, m, s -> LazyColumnRenderer(c, m, s) }
        register("LazyRow") { c, m, s -> LazyRowRenderer(c, m, s) }
        register("LazyGrid") { c, m, s -> LazyGridRenderer(c, m, s) }
        register("Conditional") { c, m, s -> ConditionalRenderer(c, m, s) }
        register("Visible") { c, m, s -> VisibleRenderer(c, m, s) }
        register("Scaffold") { c, m, s -> ScaffoldRenderer(c, m, s) }
        register("IconButton") { c, m, s -> IconButtonRenderer(c, m, s) }
        register("CircularProgress") { c, m, s -> CircularProgressRenderer(c, m, s) }
        register("LinearProgress") { c, m, s -> LinearProgressRenderer(c, m, s) }
    }

    /** Registers a custom or override renderer by [type]. */
    public fun register(type: String, renderer: ComponentRenderer) {
        renderers[type] = renderer
    }

    /** Resolves a renderer by [type], returning null for unknown types. */
    public fun resolve(type: String): ComponentRenderer? = renderers[type]

    /** Resets to default built-in renderers. Intended for tests only. */
    public fun reset() {
        renderers.clear()
        registerBuiltIns()
    }
}
