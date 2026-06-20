package `in`.shanudevcodes.sdui.core.renderer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import `in`.shanudevcodes.sdui.core.schema.SduiStyleDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StyleResolverTest {

    @Test
    fun testResolve_nullStyle_returnsDefault() {
        val resolved = StyleResolver.resolve(null)
        assertEquals(TextStyle.Default, resolved)
    }

    @Test
    fun testResolve_fullStyle_mapsCorrectly() {
        val styleDto = SduiStyleDto(
            fontSize = 18,
            fontWeight = "Bold",
            fontStyle = "Italic",
            color = "#FF00FF",
            textAlign = "Center",
            letterSpacing = 0.5f,
            lineHeight = 28,
            textDecoration = "Underline"
        )

        val resolved = StyleResolver.resolve(styleDto)

        assertEquals(18.sp, resolved.fontSize)
        assertEquals(FontWeight.Bold, resolved.fontWeight)
        assertEquals(FontStyle.Italic, resolved.fontStyle)
        assertEquals(Color(0xFFFF00FF), resolved.color)
        assertEquals(TextAlign.Center, resolved.textAlign)
        assertEquals(0.5f.sp, resolved.letterSpacing)
        assertEquals(28.sp, resolved.lineHeight)
        assertEquals(TextDecoration.Underline, resolved.textDecoration)
    }

    @Test
    fun testResolve_invalidValues_fallbacksSafely() {
        val styleDto = SduiStyleDto(
            fontWeight = "InvalidWeight",
            fontStyle = "InvalidStyle",
            textAlign = "InvalidAlign",
            color = "InvalidColor",
            textDecoration = "InvalidDecoration"
        )

        val resolved = StyleResolver.resolve(styleDto)

        assertNull(resolved.fontWeight, "Invalid fontWeight must resolve to null")
        assertNull(resolved.fontStyle, "Invalid fontStyle must resolve to null")
        assertEquals(TextAlign.Unspecified, resolved.textAlign, "Invalid textAlign must fallback to Unspecified")
        assertEquals(Color.Unspecified, resolved.color, "Invalid color must fallback to unspecified")
        assertNull(resolved.textDecoration, "Invalid decoration must resolve to null")
    }
}
