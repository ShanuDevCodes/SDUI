package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Renderer for the Slider component. Binds to a numeric state key, supporting custom float range and steps.
 */
@Composable
fun SliderRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val enabled = component.booleanProp("enabled", true)

    val min = component.props["valueRangeMin"]?.jsonPrimitive?.floatOrNull ?: 0f
    val max = component.props["valueRangeMax"]?.jsonPrimitive?.floatOrNull ?: 1f
    val steps = component.intProp("steps", 0)

    val stateMap by stateHolder.state.collectAsState()
    val floatValue = if (stateKey.isNotEmpty()) {
        stateMap[stateKey]?.jsonPrimitive?.floatOrNull ?: min
    } else {
        min
    }

    var localValue by remember { mutableStateOf(min) }
    val displayValue = if (stateKey.isNotEmpty()) floatValue else localValue

    Slider(
        value = displayValue.coerceIn(min, max),
        onValueChange = { newValue ->
            if (stateKey.isNotEmpty()) {
                stateHolder.setValue(stateKey, JsonPrimitive(newValue))
            } else {
                localValue = newValue
            }
        },
        valueRange = min..max,
        steps = steps,
        modifier = modifier,
        enabled = enabled
    )
}
