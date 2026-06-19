package `in`.shanudevcodes.sdui.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalTestApi::class)
class SduiThemeResolverTest {

    @Test
    fun testParseColor_validHex() {
        // 6-digit hex (defaults to fully opaque alpha)
        assertEquals(Color(0xFFFF0000), SduiThemeResolver.parseColor("#FF0000"))
        assertEquals(Color(0xFF00FF00), SduiThemeResolver.parseColor("#00FF00"))
        assertEquals(Color(0xFF0000FF), SduiThemeResolver.parseColor("#0000FF"))

        // 8-digit hex (includes alpha channel)
        assertEquals(Color(0x80FF0000), SduiThemeResolver.parseColor("#80FF0000"))
        assertEquals(Color(0x00FF0000), SduiThemeResolver.parseColor("#00FF0000"))
    }

    @Test
    fun testParseColor_invalidHex_returnsNull() {
        assertNull(SduiThemeResolver.parseColor(null))
        assertNull(SduiThemeResolver.parseColor("red"))
        assertNull(SduiThemeResolver.parseColor("#FF"))
        assertNull(SduiThemeResolver.parseColor("#FG0000")) // invalid hex character 'G'
        assertNull(SduiThemeResolver.parseColor("FF0000"))  // missing '#' prefix
    }

    @Test
    fun testSduiTheme_colorOverridesApplied() = runComposeUiTest {
        var primaryColor: Color? = null
        var backgroundColor: Color? = null

        setContent {
            val themeOverrides = mapOf(
                "primaryColor" to "#FF0000",
                "backgroundColor" to "#00FF00"
            )
            SduiThemeResolver.SduiTheme(theme = themeOverrides) {
                primaryColor = MaterialTheme.colorScheme.primary
                backgroundColor = MaterialTheme.colorScheme.background
            }
        }

        assertEquals(Color(0xFFFF0000), primaryColor)
        assertEquals(Color(0xFF00FF00), backgroundColor)
    }

    @Test
    fun testSduiTheme_fallbackToHostTheme() = runComposeUiTest {
        var primaryColor: Color? = null
        var defaultErrorColor: Color? = null
        var resolvedErrorColor: Color? = null

        setContent {
            defaultErrorColor = MaterialTheme.colorScheme.error
            val themeOverrides = mapOf(
                "primaryColor" to "#FF0000"
                // errorColor is left out
            )
            SduiThemeResolver.SduiTheme(theme = themeOverrides) {
                primaryColor = MaterialTheme.colorScheme.primary
                resolvedErrorColor = MaterialTheme.colorScheme.error
            }
        }

        assertEquals(Color(0xFFFF0000), primaryColor)
        assertEquals(defaultErrorColor, resolvedErrorColor)
    }
}
