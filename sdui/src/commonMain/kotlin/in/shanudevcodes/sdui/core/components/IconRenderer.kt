package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.resolvedStringProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Renders an icon from a remote URL via Coil.
 * Props:
 *   url  — required: any image URL (PNG, SVG, WebP). Use a CDN like Google Fonts Icons SVG.
 *   tint — optional: hex color applied as a ColorFilter tint.
 *   name — optional: used as contentDescription only.
 *
 * Example CDN usage (Google Material Symbols):
 *   "url": "https://fonts.gstatic.com/s/i/materialiconsoutlined/search/v1/24px.svg"
 */
@Composable
internal fun IconRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val state by stateHolder.state.collectAsState()
    val url = component.resolvedStringProp("url", state)
    val name = component.stringProp("name")
    val tintStr = component.stringProp("tint")
    val tint = parseIconHexColor(tintStr)

    // Default to 24dp if no size modifier was provided in JSON
    val hasSizeModifier = component.modifiers.any { it.type in setOf("size", "width", "height", "fillMaxSize", "fillMaxWidth", "fillMaxHeight") }
    val resolvedModifier = if (hasSizeModifier) modifier else modifier.size(24.dp)

    AsyncImage(
        model = url,
        contentDescription = name,
        modifier = resolvedModifier,
        contentScale = ContentScale.Fit,
        colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
    )
}

private fun parseIconHexColor(hex: String?): Color {
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
