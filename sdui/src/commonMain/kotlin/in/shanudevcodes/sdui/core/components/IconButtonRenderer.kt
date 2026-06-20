package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.action
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Renders a tappable icon button. The icon is a URL (PNG/SVG from CDN).
 * Props: url, icon (name/CDN key), tint, contentDescription, enabled.
 */
@Composable
internal fun IconButtonRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val url = component.stringProp("url")
    val contentDesc = component.stringProp("contentDescription")
    val enabled = component.booleanProp("enabled", true)
    val tintStr = component.stringProp("tint")
    val onClickAction = component.action("onClick")
    val onAction = LocalSduiActionHandler.current

    val tint = parseIBColor(tintStr)

    // Apply contentDescription to the button container so it's discoverable regardless of icon source
    val buttonModifier = if (contentDesc.isNotEmpty())
        modifier.semantics { contentDescription = contentDesc }
    else modifier

    IconButton(
        onClick = { onClickAction?.let { onAction(it) } },
        modifier = buttonModifier,
        enabled = enabled
    ) {
        if (url.isNotEmpty()) {
            AsyncImage(
                model = url,
                contentDescription = null, // already on parent
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.Fit,
                colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
            )
        }
    }
}

private fun parseIBColor(hex: String?): Color {
    if (hex.isNullOrEmpty()) return Color.Unspecified
    val c = hex.removePrefix("#")
    return try {
        when (c.length) {
            6 -> Color(c.toLong(16) or 0xFF000000)
            8 -> Color(c.toLong(16))
            else -> Color.Unspecified
        }
    } catch (e: Exception) { Color.Unspecified }
}
