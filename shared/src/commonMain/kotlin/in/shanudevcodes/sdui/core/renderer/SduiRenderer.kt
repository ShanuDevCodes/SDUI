package `in`.shanudevcodes.sdui.core.renderer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * CompositionLocal to expose the action dispatcher lambda to all composable renderers.
 */
val LocalSduiActionHandler = staticCompositionLocalOf<(SduiActionDto) -> Unit> {
    { _ -> }
}

/**
 * CompositionLocal to manage and track the current recursion depth of the rendering tree.
 */
val LocalSduiDepth = staticCompositionLocalOf { 0 }

/**
 * Core recursive rendering engine. Resolves component types, applies modifier chains,
 * checks recursion depth limits, and calls Compose renderers.
 */
@Composable
fun SduiRenderer(
    component: SduiComponentDto,
    stateHolder: SduiStateHolder,
    modifier: Modifier = Modifier
) {
    val currentDepth = LocalSduiDepth.current
    if (currentDepth > 50) {
        println("ERROR: Max recursion depth (50) exceeded at component type: ${component.type}. Aborting branch.")
        return
    }

    val onAction = LocalSduiActionHandler.current
    val resolvedModifier = ModifierResolver.resolve(component.modifiers, onAction)
    val renderer = ComponentRegistry.resolve(component.type)

    if (renderer != null) {
        CompositionLocalProvider(LocalSduiDepth provides (currentDepth + 1)) {
            renderer(component, modifier.then(resolvedModifier), stateHolder)
        }
    } else {
        println("WARNING: Unresolved component type: ${component.type}. Skipping node rendering.")
    }
}
