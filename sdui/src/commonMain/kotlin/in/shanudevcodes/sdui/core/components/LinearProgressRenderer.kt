package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonPrimitive

@Composable
internal fun LinearProgressRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val progress = component.props["progress"]?.jsonPrimitive?.floatOrNull ?: -1f
    val color = parseLinearColor(component.stringProp("color"))
    val trackColor = parseLinearColor(component.stringProp("trackColor"))

    if (progress < 0f) {
        when {
            color != Color.Unspecified && trackColor != Color.Unspecified ->
                LinearProgressIndicator(modifier = modifier, color = color, trackColor = trackColor)
            color != Color.Unspecified ->
                LinearProgressIndicator(modifier = modifier, color = color)
            else ->
                LinearProgressIndicator(modifier = modifier)
        }
    } else {
        when {
            color != Color.Unspecified && trackColor != Color.Unspecified ->
                LinearProgressIndicator(progress = { progress }, modifier = modifier, color = color, trackColor = trackColor)
            color != Color.Unspecified ->
                LinearProgressIndicator(progress = { progress }, modifier = modifier, color = color)
            else ->
                LinearProgressIndicator(progress = { progress }, modifier = modifier)
        }
    }
}

private fun parseLinearColor(hexColorString: String?): Color {
    if (hexColorString.isNullOrEmpty()) return Color.Unspecified
    val cleanHex = hexColorString.removePrefix("#")
    return try {
        when (cleanHex.length) {
            6 -> Color(cleanHex.toLong(16) or 0xFF000000)
            8 -> Color(cleanHex.toLong(16))
            else -> Color.Unspecified
        }
    } catch (e: Exception) {
        Color.Unspecified
    }
}
