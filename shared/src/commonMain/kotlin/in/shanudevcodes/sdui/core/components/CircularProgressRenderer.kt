package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun CircularProgressRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val progress = component.props["progress"]?.jsonPrimitive?.floatOrNull ?: -1f
    val color = parseProgressColor(component.stringProp("color"))

    if (progress < 0f) {
        if (color != Color.Unspecified) {
            CircularProgressIndicator(modifier = modifier, color = color)
        } else {
            CircularProgressIndicator(modifier = modifier)
        }
    } else {
        if (color != Color.Unspecified) {
            CircularProgressIndicator(progress = { progress }, modifier = modifier, color = color)
        } else {
            CircularProgressIndicator(progress = { progress }, modifier = modifier)
        }
    }
}

private fun parseProgressColor(hexColorString: String?): Color {
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
