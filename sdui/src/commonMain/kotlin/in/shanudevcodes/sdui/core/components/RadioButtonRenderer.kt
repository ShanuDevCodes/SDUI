package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.serialization.json.JsonPrimitive

/**
 * Renderer for the RadioButton component. Binds to a specific value and state key.
 * Becomes selected when state[stateKey] == value.
 */
@Composable
internal fun RadioButtonRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val valueElement = component.props["value"]
    val enabled = component.booleanProp("enabled", true)

    val stateMap by stateHolder.state.collectAsState()
    val isSelected = if (stateKey.isNotEmpty() && valueElement != null) {
        stateMap[stateKey] == valueElement
    } else {
        false
    }

    var localSelected by remember { mutableStateOf(false) }
    val displaySelected = if (stateKey.isNotEmpty() && valueElement != null) isSelected else localSelected

    RadioButton(
        selected = displaySelected,
        onClick = {
            if (stateKey.isNotEmpty() && valueElement != null) {
                stateHolder.setValue(stateKey, valueElement)
            } else {
                localSelected = true
            }
        },
        modifier = modifier,
        enabled = enabled
    )
}
