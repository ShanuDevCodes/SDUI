package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import `in`.shanudevcodes.sdui.core.renderer.StyleResolver
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.schema.styleProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun TextRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val text = component.stringProp("text")
    val styleDto = component.styleProp()
    val textStyle = StyleResolver.resolve(styleDto)
    val maxLines = styleDto?.maxLines ?: Int.MAX_VALUE
    val overflow = when (styleDto?.overflow) {
        "Clip" -> TextOverflow.Clip
        "Ellipsis" -> TextOverflow.Ellipsis
        "Visible" -> TextOverflow.Visible
        else -> TextOverflow.Clip
    }

    Text(
        text = text,
        modifier = modifier,
        style = textStyle,
        maxLines = maxLines,
        overflow = overflow
    )
}
