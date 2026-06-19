package `in`.shanudevcodes.sdui.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * Resolver for server-defined styling and theme overrides.
 */
object SduiThemeResolver {

    /**
     * Wraps the content in a MaterialTheme with custom color scheme, typography, and shape overrides.
     * Unspecified values fall back to the host application's theme values.
     */
    @Composable
    fun SduiTheme(
        theme: Map<String, String>,
        content: @Composable () -> Unit
    ) {
        val current = MaterialTheme.colorScheme
        val customColorScheme = current.copy(
            primary = parseColor(theme["primaryColor"]) ?: current.primary,
            onPrimary = parseColor(theme["onPrimaryColor"]) ?: current.onPrimary,
            primaryContainer = parseColor(theme["primaryContainerColor"]) ?: current.primaryContainer,
            onPrimaryContainer = parseColor(theme["onPrimaryContainerColor"]) ?: current.onPrimaryContainer,
            secondary = parseColor(theme["secondaryColor"]) ?: current.secondary,
            onSecondary = parseColor(theme["onSecondaryColor"]) ?: current.onSecondary,
            secondaryContainer = parseColor(theme["secondaryContainerColor"]) ?: current.secondaryContainer,
            onSecondaryContainer = parseColor(theme["onSecondaryContainerColor"]) ?: current.onSecondaryContainer,
            tertiary = parseColor(theme["tertiaryColor"]) ?: current.tertiary,
            onTertiary = parseColor(theme["onTertiaryColor"]) ?: current.onTertiary,
            tertiaryContainer = parseColor(theme["tertiaryContainerColor"]) ?: current.tertiaryContainer,
            onTertiaryContainer = parseColor(theme["onTertiaryContainerColor"]) ?: current.onTertiaryContainer,
            background = parseColor(theme["backgroundColor"]) ?: current.background,
            onBackground = parseColor(theme["onBackgroundColor"]) ?: current.onBackground,
            surface = parseColor(theme["surfaceColor"]) ?: current.surface,
            onSurface = parseColor(theme["onSurfaceColor"]) ?: current.onSurface,
            surfaceVariant = parseColor(theme["surfaceVariantColor"]) ?: current.surfaceVariant,
            onSurfaceVariant = parseColor(theme["onSurfaceVariantColor"]) ?: current.onSurfaceVariant,
            error = parseColor(theme["errorColor"]) ?: current.error,
            onError = parseColor(theme["onErrorColor"]) ?: current.onError,
            errorContainer = parseColor(theme["errorContainerColor"]) ?: current.errorContainer,
            onErrorContainer = parseColor(theme["onErrorContainerColor"]) ?: current.onErrorContainer,
            outline = parseColor(theme["outlineColor"]) ?: current.outline,
            outlineVariant = parseColor(theme["outlineVariantColor"]) ?: current.outlineVariant,
            scrim = parseColor(theme["scrimColor"]) ?: current.scrim,
            inverseSurface = parseColor(theme["inverseSurfaceColor"]) ?: current.inverseSurface,
            inverseOnSurface = parseColor(theme["inverseOnSurfaceColor"]) ?: current.inverseOnSurface,
            inversePrimary = parseColor(theme["inversePrimaryColor"]) ?: current.inversePrimary,
            surfaceTint = parseColor(theme["surfaceTintColor"]) ?: current.surfaceTint
        )

        val baseTypography = MaterialTheme.typography
        val customTypography = Typography(
            displayLarge = baseTypography.displayLarge.maybeWithSize(theme["typographyDisplayLargeFontSize"]),
            displayMedium = baseTypography.displayMedium.maybeWithSize(theme["typographyDisplayMediumFontSize"]),
            displaySmall = baseTypography.displaySmall.maybeWithSize(theme["typographyDisplaySmallFontSize"]),
            headlineLarge = baseTypography.headlineLarge.maybeWithSize(theme["typographyHeadlineLargeFontSize"]),
            headlineMedium = baseTypography.headlineMedium.maybeWithSize(theme["typographyHeadlineMediumFontSize"]),
            headlineSmall = baseTypography.headlineSmall.maybeWithSize(theme["typographyHeadlineSmallFontSize"]),
            titleLarge = baseTypography.titleLarge.maybeWithSize(theme["typographyTitleLargeFontSize"]),
            titleMedium = baseTypography.titleMedium.maybeWithSize(theme["typographyTitleMediumFontSize"]),
            titleSmall = baseTypography.titleSmall.maybeWithSize(theme["typographyTitleSmallFontSize"]),
            bodyLarge = baseTypography.bodyLarge.maybeWithSize(theme["typographyBodyLargeFontSize"]),
            bodyMedium = baseTypography.bodyMedium.maybeWithSize(theme["typographyBodyMediumFontSize"]),
            bodySmall = baseTypography.bodySmall.maybeWithSize(theme["typographyBodySmallFontSize"]),
            labelLarge = baseTypography.labelLarge.maybeWithSize(theme["typographyLabelLargeFontSize"]),
            labelMedium = baseTypography.labelMedium.maybeWithSize(theme["typographyLabelMediumFontSize"]),
            labelSmall = baseTypography.labelSmall.maybeWithSize(theme["typographyLabelSmallFontSize"])
        )

        val baseShapes = MaterialTheme.shapes
        val customShapes = Shapes(
            extraSmall = theme["shapeExtraSmallRadius"]?.toIntOrNull()
                ?.let { RoundedCornerShape(it.dp) } ?: baseShapes.extraSmall,
            small = theme["shapeSmallRadius"]?.toIntOrNull()
                ?.let { RoundedCornerShape(it.dp) } ?: baseShapes.small,
            medium = theme["shapeMediumRadius"]?.toIntOrNull()
                ?.let { RoundedCornerShape(it.dp) } ?: baseShapes.medium,
            large = theme["shapeLargeRadius"]?.toIntOrNull()
                ?.let { RoundedCornerShape(it.dp) } ?: baseShapes.large,
            extraLarge = theme["shapeExtraLargeRadius"]?.toIntOrNull()
                ?.let { RoundedCornerShape(it.dp) } ?: baseShapes.extraLarge
        )

        MaterialTheme(
            colorScheme = customColorScheme,
            typography = customTypography,
            shapes = customShapes,
            content = content
        )
    }

    /**
     * Resolves a color token name to its ColorScheme value, or parses as hex.
     */
    fun resolveColor(token: String, colorScheme: ColorScheme): Color {
        return when (token) {
            "primary" -> colorScheme.primary
            "onPrimary" -> colorScheme.onPrimary
            "primaryContainer" -> colorScheme.primaryContainer
            "onPrimaryContainer" -> colorScheme.onPrimaryContainer
            "secondary" -> colorScheme.secondary
            "onSecondary" -> colorScheme.onSecondary
            "secondaryContainer" -> colorScheme.secondaryContainer
            "onSecondaryContainer" -> colorScheme.onSecondaryContainer
            "tertiary" -> colorScheme.tertiary
            "onTertiary" -> colorScheme.onTertiary
            "tertiaryContainer" -> colorScheme.tertiaryContainer
            "onTertiaryContainer" -> colorScheme.onTertiaryContainer
            "background" -> colorScheme.background
            "onBackground" -> colorScheme.onBackground
            "surface" -> colorScheme.surface
            "onSurface" -> colorScheme.onSurface
            "surfaceVariant" -> colorScheme.surfaceVariant
            "onSurfaceVariant" -> colorScheme.onSurfaceVariant
            "error" -> colorScheme.error
            "onError" -> colorScheme.onError
            "errorContainer" -> colorScheme.errorContainer
            "onErrorContainer" -> colorScheme.onErrorContainer
            "outline" -> colorScheme.outline
            "outlineVariant" -> colorScheme.outlineVariant
            "scrim" -> colorScheme.scrim
            "inverseSurface" -> colorScheme.inverseSurface
            "inverseOnSurface" -> colorScheme.inverseOnSurface
            "inversePrimary" -> colorScheme.inversePrimary
            "surfaceTint" -> colorScheme.surfaceTint
            else -> parseColor(token) ?: Color.Unspecified
        }
    }

    /**
     * Parses a hex color string (e.g. "#FF0000" or "#AAFF0000") to a Compose [Color].
     */
    fun parseColor(colorString: String?): Color? {
        if (colorString == null || !colorString.startsWith("#")) return null
        return try {
            val hex = colorString.substring(1)
            val colorLong = hex.toLong(16)
            when (hex.length) {
                6 -> Color(colorLong or 0xFF000000)
                8 -> Color(colorLong)
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun TextStyle.maybeWithSize(sizeStr: String?): TextStyle {
        val sp = sizeStr?.toIntOrNull() ?: return this
        return this.copy(fontSize = sp.sp)
    }
}
