package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Renderer for the DropdownMenu component. Binds to a state key and renders a selection popup menu
 * based on an array of string option values.
 */
@Composable
fun DropdownMenuRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val label = component.stringProp("label")
    val enabled = component.booleanProp("enabled", true)

    val optionsElement = component.props["options"]
    val options = if (optionsElement is JsonArray) {
        optionsElement.map { it.jsonPrimitive.content }
    } else {
        emptyList()
    }

    val stateMap by stateHolder.state.collectAsState()
    val selectedValue = if (stateKey.isNotEmpty()) {
        stateMap[stateKey]?.jsonPrimitive?.content ?: ""
    } else {
        ""
    }

    var localSelected by remember { mutableStateOf(if (options.isNotEmpty()) options[0] else "") }
    val displayValue = if (stateKey.isNotEmpty()) selectedValue else localSelected

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.then(
            if (enabled) {
                Modifier.clickable { expanded = true }
            } else {
                Modifier
            }
        )
    ) {
        val displayText = if (displayValue.isNotEmpty()) {
            displayValue
        } else if (label.isNotEmpty()) {
            label
        } else {
            "Select..."
        }

        Text(text = displayText)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        if (stateKey.isNotEmpty()) {
                            stateHolder.setValue(stateKey, JsonPrimitive(option))
                        } else {
                            localSelected = option
                        }
                        expanded = false
                    }
                )
            }
        }
    }
}
