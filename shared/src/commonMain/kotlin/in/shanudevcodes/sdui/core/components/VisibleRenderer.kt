package `in`.shanudevcodes.sdui.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Renderer for the Visible component. Conditionally renders all of its children if the condition is met.
 */
@Composable
fun VisibleRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val operator = component.stringProp("operator")
    val compareValue = component.props["compareValue"]

    val stateMap by stateHolder.state.collectAsState()
    val stateValue = if (stateKey.isNotEmpty()) stateMap[stateKey] else null

    val isVisible = ConditionEvaluator.evaluate(stateValue, operator, compareValue)

    if (isVisible) {
        component.children.forEach { child ->
            SduiRenderer(child, stateHolder, modifier)
        }
    }
}
