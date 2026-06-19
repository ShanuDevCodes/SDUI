package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.renderer.StyleResolver
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.action
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.resolvedStringProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.schema.styleProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Renderer for the Button component. Supports filled/outlined/text/elevated/tonal variants,
 * optional colors, shape, and onClick actions.
 */
@Composable
fun ButtonRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val state by stateHolder.state.collectAsState()
    val enabled = component.booleanProp("enabled", true)
    val text = component.resolvedStringProp("text", state)
    val onClickAction = component.action("onClick")
    val onAction = LocalSduiActionHandler.current

    val variant = component.stringProp("variant", "filled")
    val containerColorStr = component.stringProp("containerColor")
    val contentColorStr = component.stringProp("contentColor")
    val shapeName = component.stringProp("shape")
    val radius = component.intProp("radius", 50)

    val containerColor = parseHexColor(containerColorStr)
    val contentColor = parseHexColor(contentColorStr)

    val shape = if (shapeName.isNotEmpty()) RoundedCornerShape(radius.dp) else null

    val colors = if (containerColor != Color.Unspecified || contentColor != Color.Unspecified) {
        when (variant) {
            "outlined" -> ButtonDefaults.outlinedButtonColors(
                containerColor = if (containerColor != Color.Unspecified) containerColor else Color.Transparent,
                contentColor = if (contentColor != Color.Unspecified) contentColor else Color.Unspecified
            )
            "text" -> ButtonDefaults.textButtonColors(
                containerColor = if (containerColor != Color.Unspecified) containerColor else Color.Transparent,
                contentColor = if (contentColor != Color.Unspecified) contentColor else Color.Unspecified
            )
            "elevated" -> ButtonDefaults.elevatedButtonColors(
                containerColor = if (containerColor != Color.Unspecified) containerColor else Color.Unspecified,
                contentColor = if (contentColor != Color.Unspecified) contentColor else Color.Unspecified
            )
            "tonal" -> ButtonDefaults.filledTonalButtonColors(
                containerColor = if (containerColor != Color.Unspecified) containerColor else Color.Unspecified,
                contentColor = if (contentColor != Color.Unspecified) contentColor else Color.Unspecified
            )
            else -> ButtonDefaults.buttonColors(
                containerColor = if (containerColor != Color.Unspecified) containerColor else Color.Unspecified,
                contentColor = if (contentColor != Color.Unspecified) contentColor else Color.Unspecified
            )
        }
    } else null

    val content: @Composable () -> Unit = {
        if (component.children.isNotEmpty()) {
            component.children.forEach { child -> SduiRenderer(child, stateHolder) }
        } else {
            val styleDto = component.styleProp()
            Text(text = text, style = StyleResolver.resolve(styleDto))
        }
    }

    val clickHandler: () -> Unit = { onClickAction?.let { onAction(it) } }

    when (variant) {
        "outlined" -> OutlinedButton(
            onClick = clickHandler,
            modifier = modifier,
            enabled = enabled,
            shape = shape ?: ButtonDefaults.outlinedShape,
            colors = colors ?: ButtonDefaults.outlinedButtonColors(),
            content = { content() }
        )
        "text" -> TextButton(
            onClick = clickHandler,
            modifier = modifier,
            enabled = enabled,
            shape = shape ?: ButtonDefaults.textShape,
            colors = colors ?: ButtonDefaults.textButtonColors(),
            content = { content() }
        )
        "elevated" -> ElevatedButton(
            onClick = clickHandler,
            modifier = modifier,
            enabled = enabled,
            shape = shape ?: ButtonDefaults.elevatedShape,
            colors = colors ?: ButtonDefaults.elevatedButtonColors(),
            content = { content() }
        )
        "tonal" -> FilledTonalButton(
            onClick = clickHandler,
            modifier = modifier,
            enabled = enabled,
            shape = shape ?: ButtonDefaults.filledTonalShape,
            colors = colors ?: ButtonDefaults.filledTonalButtonColors(),
            content = { content() }
        )
        else -> Button(
            onClick = clickHandler,
            modifier = modifier,
            enabled = enabled,
            shape = shape ?: ButtonDefaults.shape,
            colors = colors ?: ButtonDefaults.buttonColors(),
            content = { content() }
        )
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
