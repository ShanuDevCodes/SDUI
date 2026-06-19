package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.renderer.StyleResolver
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.action
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.schema.styleProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Renderer for the Button component. Supports text labels, recursively rendering children,
 * and dispatching onClick actions.
 */
@Composable
fun ButtonRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val enabled = component.booleanProp("enabled", true)
    val text = component.stringProp("text")
    val onClickAction = component.action("onClick")
    val onAction = LocalSduiActionHandler.current

    Button(
        onClick = { onClickAction?.let { onAction(it) } },
        modifier = modifier,
        enabled = enabled
    ) {
        if (component.children.isNotEmpty()) {
            component.children.forEach { child ->
                SduiRenderer(child, stateHolder)
            }
        } else {
            val styleDto = component.styleProp()
            val textStyle = StyleResolver.resolve(styleDto)
            Text(
                text = text,
                style = textStyle
            )
        }
    }
}
