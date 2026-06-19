package `in`.shanudevcodes.sdui.core.renderer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import `in`.shanudevcodes.sdui.core.schema.SduiStyleDto
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiStyle

/**
 * Utility to resolve server-defined styling attributes into native Compose TextStyles.
 */
object StyleResolver {

    /**
     * Converts a domain SduiStyle configuration into a Compose TextStyle.
     */
    fun resolveDomain(style: SduiStyle?): TextStyle {
        if (style == null) return TextStyle.Default
        val dto = SduiStyleDto(
            fontSize = style.fontSize,
            fontWeight = style.fontWeight,
            fontStyle = style.fontStyle,
            color = style.color,
            textAlign = style.textAlign,
            maxLines = style.maxLines,
            overflow = style.overflow
        )
        return resolve(dto)
    }

    /**
     * Converts an SduiStyleDto configuration into a Compose TextStyle.
     */
    fun resolve(styleDto: SduiStyleDto?): TextStyle {
        if (styleDto == null) return TextStyle.Default

        val color = parseHexColor(styleDto.color)
        val fontSize = styleDto.fontSize?.sp ?: TextStyle.Default.fontSize
        val fontWeight = parseFontWeight(styleDto.fontWeight)
        val fontStyle = parseFontStyle(styleDto.fontStyle)
        val textAlign = parseTextAlign(styleDto.textAlign) ?: TextAlign.Unspecified
        val letterSpacing = styleDto.letterSpacing?.sp ?: TextStyle.Default.letterSpacing
        val lineHeight = styleDto.lineHeight?.sp ?: TextStyle.Default.lineHeight
        val textDecoration = parseTextDecoration(styleDto.textDecoration)

        return TextStyle(
            color = if (color != Color.Unspecified) color else Color.Unspecified,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            textAlign = textAlign,
            letterSpacing = letterSpacing,
            lineHeight = lineHeight,
            textDecoration = textDecoration
        )
    }

    private fun parseHexColor(hexColorString: String?): Color {
        if (hexColorString == null) return Color.Unspecified
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

    private fun parseFontWeight(weight: String?): FontWeight? {
        return when (weight) {
            "Thin" -> FontWeight.Thin
            "Light" -> FontWeight.Light
            "Normal" -> FontWeight.Normal
            "Medium" -> FontWeight.Medium
            "SemiBold" -> FontWeight.SemiBold
            "Bold" -> FontWeight.Bold
            "ExtraBold" -> FontWeight.ExtraBold
            "Black" -> FontWeight.Black
            else -> null
        }
    }

    private fun parseFontStyle(style: String?): FontStyle? {
        return when (style) {
            "Normal" -> FontStyle.Normal
            "Italic" -> FontStyle.Italic
            else -> null
        }
    }

    private fun parseTextAlign(align: String?): TextAlign? {
        return when (align) {
            "Start" -> TextAlign.Start
            "End" -> TextAlign.End
            "Center" -> TextAlign.Center
            "Justify" -> TextAlign.Justify
            else -> null
        }
    }

    private fun parseTextDecoration(decoration: String?): TextDecoration? {
        return when (decoration) {
            "None" -> TextDecoration.None
            "Underline" -> TextDecoration.Underline
            "LineThrough" -> TextDecoration.LineThrough
            else -> null
        }
    }
}
