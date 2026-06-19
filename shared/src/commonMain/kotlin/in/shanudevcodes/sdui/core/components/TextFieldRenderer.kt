package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Renderer for the TextField component. Supports two-way binding to stateHolder,
 * configurable keyboard options, and password masking.
 */
@Composable
fun TextFieldRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val label = component.stringProp("label")
    val placeholder = component.stringProp("placeholder")
    val enabled = component.booleanProp("enabled", true)
    val singleLine = component.booleanProp("singleLine", true)

    val stateMap by stateHolder.state.collectAsState()
    val textValue = if (stateKey.isNotEmpty()) {
        stateMap[stateKey]?.jsonPrimitive?.content ?: ""
    } else {
        ""
    }

    var localText by remember { mutableStateOf("") }
    val displayValue = if (stateKey.isNotEmpty()) textValue else localText

    val keyboardTypeStr = component.stringProp("keyboardType", "Text")
    val keyboardOptions = KeyboardOptions(
        keyboardType = when (keyboardTypeStr) {
            "Number" -> KeyboardType.Number
            "Email" -> KeyboardType.Email
            "Password" -> KeyboardType.Password
            "Phone" -> KeyboardType.Phone
            "Uri" -> KeyboardType.Uri
            else -> KeyboardType.Text
        }
    )

    val visualTransformationStr = component.stringProp("visualTransformation")
    val visualTransformation = when (visualTransformationStr) {
        "Password" -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    TextField(
        value = displayValue,
        onValueChange = { newValue ->
            if (stateKey.isNotEmpty()) {
                stateHolder.setValue(stateKey, JsonPrimitive(newValue))
            } else {
                localText = newValue
            }
        },
        modifier = modifier,
        enabled = enabled,
        singleLine = singleLine,
        label = if (label.isNotEmpty()) { { Text(label) } } else null,
        placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder) } } else null,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation
    )
}
