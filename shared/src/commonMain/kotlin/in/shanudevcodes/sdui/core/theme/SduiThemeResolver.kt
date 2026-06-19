package `in`.shanudevcodes.sdui.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Resolver for server-defined styling and theme overrides.
 */
object SduiThemeResolver {

    /**
     * Wraps the content in a MaterialTheme with custom color scheme overrides.
     * Unspecified colors fall back to the host application's theme values.
     */
    @Composable
    fun SduiTheme(
        theme: Map<String, String>,
        content: @Composable () -> Unit
    ) {
        val currentColorScheme = MaterialTheme.colorScheme
        val customColorScheme = currentColorScheme.copy(
            primary = parseColor(theme["primaryColor"]) ?: currentColorScheme.primary,
            onPrimary = parseColor(theme["onPrimaryColor"]) ?: currentColorScheme.onPrimary,
            primaryContainer = parseColor(theme["primaryContainerColor"]) ?: currentColorScheme.primaryContainer,
            onPrimaryContainer = parseColor(theme["onPrimaryContainerColor"]) ?: currentColorScheme.onPrimaryContainer,
            secondary = parseColor(theme["secondaryColor"]) ?: currentColorScheme.secondary,
            onSecondary = parseColor(theme["onSecondaryColor"]) ?: currentColorScheme.onSecondary,
            background = parseColor(theme["backgroundColor"]) ?: currentColorScheme.background,
            onBackground = parseColor(theme["onBackgroundColor"]) ?: currentColorScheme.onBackground,
            surface = parseColor(theme["surfaceColor"]) ?: currentColorScheme.surface,
            onSurface = parseColor(theme["onSurfaceColor"]) ?: currentColorScheme.onSurface,
            error = parseColor(theme["errorColor"]) ?: currentColorScheme.error,
            onError = parseColor(theme["onErrorColor"]) ?: currentColorScheme.onError
        )

        MaterialTheme(
            colorScheme = customColorScheme,
            typography = MaterialTheme.typography,
            shapes = MaterialTheme.shapes,
            content = content
        )
    }

    /**
     * Parses a hex color string (e.g. "#FF0000" or "#AAFF0000") to a Compose [Color].
     */
    fun parseColor(colorString: String?): Color? {
        if (colorString == null || !colorString.startsWith("#")) return null
        return try {
            val hex = colorString.substring(1)
            val colorLong = hex.toLong(16)
            if (hex.length == 6) {
                Color(colorLong or 0xFF000000)
            } else if (hex.length == 8) {
                Color(colorLong)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
