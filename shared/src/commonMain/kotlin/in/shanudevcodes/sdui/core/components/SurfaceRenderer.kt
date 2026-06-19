package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.action
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
    val shadowElevation = component.intProp("shadowElevation", 0)
    val colorStr = component.stringProp("color")
    val contentColorStr = component.stringProp("contentColor")
    val color = parseSurfaceColor(colorStr)
    val contentColor = parseSurfaceColor(contentColorStr)
    val shapeName = component.stringProp("shape")
    val radius = component.intProp("radius", 0)
    val borderWidth = component.intProp("borderWidth", 0)
    val borderColor = parseSurfaceColor(component.stringProp("borderColor"))
    val onClickAction = component.action("onClick")
    val onAction = LocalSduiActionHandler.current

    val shape: Shape = when (shapeName) {
        "Circle" -> CircleShape
        "RoundedCorner", "Rounded" -> RoundedCornerShape(radius.dp)
        "Rectangle" -> RectangleShape
        else -> RectangleShape
    }

    val border: BorderStroke? = if (borderWidth > 0 && borderColor != Color.Unspecified) {
        BorderStroke(borderWidth.dp, borderColor)
    } else null

    val children: @Composable () -> Unit = {
        component.children.forEach { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }

    if (onClickAction != null) {
        Surface(
            onClick = { onAction(onClickAction) },
            modifier = modifier,
            shape = shape,
            color = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.surface,
            contentColor = if (contentColor != Color.Unspecified) contentColor else MaterialTheme.colorScheme.onSurface,
            tonalElevation = elevation.dp,
            shadowElevation = shadowElevation.dp,
            border = border,
            content = { children() }
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = if (color != Color.Unspecified) color else MaterialTheme.colorScheme.surface,
            contentColor = if (contentColor != Color.Unspecified) contentColor else MaterialTheme.colorScheme.onSurface,
            tonalElevation = elevation.dp,
            shadowElevation = shadowElevation.dp,
            border = border,
            content = { children() }
        )
    }
}

private fun parseSurfaceColor(hexColorString: String?): Color {
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
