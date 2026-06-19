package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.Checkbox
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
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Renderer for the Checkbox component. Binds to a boolean state key for two-way checked status updates.
 */
@Composable
fun CheckboxRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val enabled = component.booleanProp("enabled", true)

    val stateMap by stateHolder.state.collectAsState()
    val isChecked = if (stateKey.isNotEmpty()) {
        stateMap[stateKey]?.jsonPrimitive?.booleanOrNull ?: false
    } else {
        false
    }

    var localChecked by remember { mutableStateOf(false) }
    val displayChecked = if (stateKey.isNotEmpty()) isChecked else localChecked

    Checkbox(
        checked = displayChecked,
        onCheckedChange = { newValue ->
            if (stateKey.isNotEmpty()) {
                stateHolder.setValue(stateKey, JsonPrimitive(newValue))
            } else {
                localChecked = newValue
            }
        },
        modifier = modifier,
        enabled = enabled
    )
}
