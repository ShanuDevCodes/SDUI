package `in`.shanudevcodes.sdui.core.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.serialization.json.Json

/**
 * Renderer for the Conditional component. Evaluates logic against SduiStateHolder and renders
 * either a 'then' component or an 'else' component.
 */
@Composable
fun ConditionalRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val operator = component.stringProp("operator")
    val compareValue = component.props["compareValue"]

    val stateMap by stateHolder.state.collectAsState()
    val stateValue = if (stateKey.isNotEmpty()) stateMap[stateKey] else null

    val isConditionTrue = ConditionEvaluator.evaluate(stateValue, operator, compareValue)

    val thenComponent = component.props["then"]?.let {
        try {
            Json.decodeFromJsonElement(SduiComponentDto.serializer(), it)
        } catch (e: Exception) {
            null
        }
    }
    val elseComponent = component.props["else"]?.let {
        try {
            Json.decodeFromJsonElement(SduiComponentDto.serializer(), it)
        } catch (e: Exception) {
            null
        }
    }

    AnimatedVisibility(visible = isConditionTrue) {
        val thenComp = thenComponent ?: component.children.getOrNull(0)
        if (thenComp != null) SduiRenderer(thenComp, stateHolder, modifier)
    }
    AnimatedVisibility(visible = !isConditionTrue) {
        val elseComp = elseComponent ?: component.children.getOrNull(1)
        if (elseComp != null) SduiRenderer(elseComp, stateHolder, modifier)
    }
}
