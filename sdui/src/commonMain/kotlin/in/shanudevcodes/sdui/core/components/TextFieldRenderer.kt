package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
import `in`.shanudevcodes.sdui.core.schema.resolvedStringProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Renderer for the TextField component. Supports outlined (default) and filled variants,
 * supportingText, isError, leading/trailing icons, two-way state binding.
 */
@Composable
internal fun TextFieldRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val stateKey = component.stringProp("stateKey")
    val stateMap by stateHolder.state.collectAsState()
    val label = component.resolvedStringProp("label", stateMap)
    val placeholder = component.resolvedStringProp("placeholder", stateMap)
    val enabled = component.booleanProp("enabled", true)
    val singleLine = component.booleanProp("singleLine", true)
    val variant = component.stringProp("variant", "outlined")
    val supportingText = component.resolvedStringProp("supportingText", stateMap)
    val isError = component.booleanProp("isError", false)
    val leadingIcon = component.stringProp("leadingIcon")
    val trailingIcon = component.stringProp("trailingIcon")

    val textValue = if (stateKey.isNotEmpty()) {
        stateMap[stateKey]?.jsonPrimitive?.content ?: ""
    } else ""

    var localText by remember { mutableStateOf("") }
    val displayValue = if (stateKey.isNotEmpty()) textValue else localText

    val keyboardOptions = KeyboardOptions(
        keyboardType = when (component.stringProp("keyboardType", "Text")) {
            "Number" -> KeyboardType.Number
            "Email" -> KeyboardType.Email
            "Password" -> KeyboardType.Password
            "Phone" -> KeyboardType.Phone
            "Uri" -> KeyboardType.Uri
            else -> KeyboardType.Text
        }
    )

    val visualTransformation = when (component.stringProp("visualTransformation")) {
        "Password" -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val onValueChange: (String) -> Unit = { newValue ->
        if (stateKey.isNotEmpty()) stateHolder.setValue(stateKey, JsonPrimitive(newValue))
        else localText = newValue
    }

    val leadingIconSlot: (@Composable () -> Unit)? = if (leadingIcon.isNotEmpty()) {
        { AsyncImage(model = leadingIcon, contentDescription = null, modifier = Modifier.size(20.dp), contentScale = ContentScale.Fit) }
    } else null

    val trailingIconSlot: (@Composable () -> Unit)? = if (trailingIcon.isNotEmpty()) {
        { AsyncImage(model = trailingIcon, contentDescription = null, modifier = Modifier.size(20.dp), contentScale = ContentScale.Fit) }
    } else null

    val supportingTextSlot: (@Composable () -> Unit)? =
        if (supportingText.isNotEmpty()) { { Text(supportingText) } } else null

    if (variant == "filled") {
        TextField(
            value = displayValue,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            singleLine = singleLine,
            label = if (label.isNotEmpty()) { { Text(label) } } else null,
            placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder) } } else null,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            isError = isError,
            supportingText = supportingTextSlot,
            leadingIcon = leadingIconSlot,
            trailingIcon = trailingIconSlot
        )
    } else {
        OutlinedTextField(
            value = displayValue,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            singleLine = singleLine,
            label = if (label.isNotEmpty()) { { Text(label) } } else null,
            placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder) } } else null,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            isError = isError,
            supportingText = supportingTextSlot,
            leadingIcon = leadingIconSlot,
            trailingIcon = trailingIconSlot
        )
    }
}


