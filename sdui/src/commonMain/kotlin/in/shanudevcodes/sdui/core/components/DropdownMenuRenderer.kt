package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
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
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Renderer for the DropdownMenu component using ExposedDropdownMenuBox.
 * Supports plain string options (backward compat) and {label, value} object options.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropdownMenuRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val label = component.stringProp("label")
    val enabled = component.booleanProp("enabled", true)

    // Parse options supporting both plain strings and {label, value} objects
    data class DropdownOption(val label: String, val value: String)

    val optionsElement = component.props["options"]
    val options: List<DropdownOption> = if (optionsElement is JsonArray) {
        optionsElement.map { element ->
            when (element) {
                is JsonObject -> {
                    val lbl = element.jsonObject["label"]?.jsonPrimitive?.content ?: ""
                    val v = element.jsonObject["value"]?.jsonPrimitive?.content ?: lbl
                    DropdownOption(lbl, v)
                }
                else -> {
                    val s = element.jsonPrimitive.content
                    DropdownOption(s, s)
                }
            }
        }
    } else emptyList()

    val stateMap by stateHolder.state.collectAsState()
    val selectedValue = if (stateKey.isNotEmpty()) {
        stateMap[stateKey]?.jsonPrimitive?.content ?: ""
    } else ""

    var localSelected by remember { mutableStateOf(if (options.isNotEmpty()) options[0].value else "") }
    val displayValue = if (stateKey.isNotEmpty()) selectedValue else localSelected
    val selectedLabel = options.firstOrNull { it.value == displayValue }?.label ?: displayValue

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { if (enabled) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedLabel.ifEmpty { label },
            onValueChange = {},
            readOnly = true,
            label = if (label.isNotEmpty()) { { Text(label) } } else null,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded && enabled) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            enabled = enabled,
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded && enabled,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        if (stateKey.isNotEmpty()) {
                            stateHolder.setValue(stateKey, JsonPrimitive(option.value))
                        } else {
                            localSelected = option.value
                        }
                        expanded = false
                    }
                )
            }
        }
    }
}
