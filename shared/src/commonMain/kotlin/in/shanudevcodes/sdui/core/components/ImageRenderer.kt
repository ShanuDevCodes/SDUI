package `in`.shanudevcodes.sdui.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.resolvedStringProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun ImageRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val state by stateHolder.state.collectAsState()
    val url = component.stringProp("url")
    val contentDescription = component.resolvedStringProp("contentDescription", state)
    val contentScale = when (component.stringProp("contentScale")) {
        "Crop", "CenterCrop" -> ContentScale.Crop
        "Fit", "FitCenter" -> ContentScale.Fit
        "Fill", "FillBounds" -> ContentScale.FillBounds
        "Inside" -> ContentScale.Inside
        "None" -> ContentScale.None
        else -> ContentScale.Fit
    }

    val placeholderColor = component.stringProp("placeholderColor", "#FF2A2A2A")
    val errorTint = component.stringProp("errorTint", "#FFAAAAAA")

    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = ColorPainter(parseImageHexColor(placeholderColor)),
        error = ColorPainter(parseImageHexColor(errorTint))
    )
}

private fun parseImageHexColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    return try {
        when (clean.length) {
            6 -> Color(clean.toLong(16) or 0xFF000000)
            8 -> Color(clean.toLong(16))
            else -> Color.Unspecified
        }
    } catch (e: Exception) {
        Color.Unspecified
    }
}
