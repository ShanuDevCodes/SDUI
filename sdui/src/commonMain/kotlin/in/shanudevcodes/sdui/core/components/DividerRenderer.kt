package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
internal fun DividerRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val thicknessValue = component.intProp("thickness", 1)
    val colorStr = component.stringProp("color")
    val color = parseHexColor(colorStr)

    HorizontalDivider(
        modifier = modifier,
        thickness = thicknessValue.dp,
        color = if (color != Color.Unspecified) color else androidx.compose.material3.DividerDefaults.color
    )
}

private fun parseHexColor(hexColorString: String?): Color {
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
