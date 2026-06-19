package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun SurfaceRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val elevation = component.intProp("elevation", 0)
    val colorStr = component.stringProp("color")
    val color = parseHexColor(colorStr)
    val shapeName = component.stringProp("shape")
    val radius = component.intProp("radius", 0)

    val shape: Shape = when (shapeName) {
        "Circle" -> CircleShape
        "RoundedCorner", "Rounded" -> RoundedCornerShape(radius.dp)
        "Rectangle" -> RectangleShape
        else -> RectangleShape
    }

    Surface(
        modifier = modifier,
        shape = shape,
        color = if (color != Color.Unspecified) color else androidx.compose.material3.MaterialTheme.colorScheme.surface,
        tonalElevation = elevation.dp
    ) {
        component.children.forEach { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }
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
