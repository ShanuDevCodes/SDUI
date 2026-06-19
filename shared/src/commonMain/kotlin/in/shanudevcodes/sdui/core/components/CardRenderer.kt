package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun CardRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val elevation = component.intProp("elevation", 1)
    val shapeName = component.stringProp("shape")
    val radius = component.intProp("radius", 8)
    val onClickAction = component.action("onClick")
    val onAction = LocalSduiActionHandler.current

    val containerColor = parseCardColor(component.stringProp("containerColor"))
    val contentColor = parseCardColor(component.stringProp("contentColor"))
    val borderWidth = component.intProp("borderWidth", 0)
    val borderColor = parseCardColor(component.stringProp("borderColor"))

    val shape: Shape = when (shapeName) {
        "Circle" -> CircleShape
        "Rectangle" -> RectangleShape
        "RoundedCorner", "Rounded" -> RoundedCornerShape(radius.dp)
        else -> RoundedCornerShape(radius.dp)
    }

    val colors = CardDefaults.cardColors(
        containerColor = if (containerColor != Color.Unspecified) containerColor else CardDefaults.cardColors().containerColor,
        contentColor = if (contentColor != Color.Unspecified) contentColor else CardDefaults.cardColors().contentColor
    )

    val border: BorderStroke? = if (borderWidth > 0 && borderColor != Color.Unspecified) {
        BorderStroke(borderWidth.dp, borderColor)
    } else null

    val children: @Composable () -> Unit = {
        component.children.forEach { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }

    if (onClickAction != null) {
        Card(
            onClick = { onAction(onClickAction) },
            modifier = modifier,
            shape = shape,
            colors = colors,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
            border = border,
            content = { children() }
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = colors,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
            border = border,
            content = { children() }
        )
    }
}

private fun parseCardColor(hexColorString: String?): Color {
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
