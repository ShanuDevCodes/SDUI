# 06 — Theming

By default SDUI renders inside the host app's `MaterialTheme` — your colors, typography, and shapes apply
automatically. A screen can override the full Material 3 theme via the `theme` map. Every override is
optional; unspecified values fall back to the host theme.

```json
"theme": {
  "primaryColor": "#FFD0BCFF",
  "onPrimaryColor": "#FF381E72",
  "backgroundColor": "#FF0F0D13",
  "surfaceColor": "#FF0F0D13",
  "onSurfaceColor": "#FFE6E1E9",
  "shapeMediumRadius": 16,
  "shapeLargeRadius": 24
}
```

All values are strings (colors are hex; radii and font sizes are integer strings).

---

## Color roles

All 29 Material 3 `ColorScheme` roles can be overridden. Key = role name + `Color`, value = hex.

| JSON key | ColorScheme role |
|----------|------------------|
| `primaryColor` | `primary` |
| `onPrimaryColor` | `onPrimary` |
| `primaryContainerColor` | `primaryContainer` |
| `onPrimaryContainerColor` | `onPrimaryContainer` |
| `secondaryColor` | `secondary` |
| `onSecondaryColor` | `onSecondary` |
| `secondaryContainerColor` | `secondaryContainer` |
| `onSecondaryContainerColor` | `onSecondaryContainer` |
| `tertiaryColor` | `tertiary` |
| `onTertiaryColor` | `onTertiary` |
| `tertiaryContainerColor` | `tertiaryContainer` |
| `onTertiaryContainerColor` | `onTertiaryContainer` |
| `backgroundColor` | `background` |
| `onBackgroundColor` | `onBackground` |
| `surfaceColor` | `surface` |
| `onSurfaceColor` | `onSurface` |
| `surfaceVariantColor` | `surfaceVariant` |
| `onSurfaceVariantColor` | `onSurfaceVariant` |
| `errorColor` | `error` |
| `onErrorColor` | `onError` |
| `errorContainerColor` | `errorContainer` |
| `onErrorContainerColor` | `onErrorContainer` |
| `outlineColor` | `outline` |
| `outlineVariantColor` | `outlineVariant` |
| `scrimColor` | `scrim` |
| `inverseSurfaceColor` | `inverseSurface` |
| `inverseOnSurfaceColor` | `inverseOnSurface` |
| `inversePrimaryColor` | `inversePrimary` |
| `surfaceTintColor` | `surfaceTint` |

Color format: `#RRGGBB` or `#AARRGGBB`. Invalid values fall back to the host role.

---

## Shape radii

Overrides the Material 3 `Shapes` corner radii (dp, integer string):

| JSON key | Shapes role |
|----------|-------------|
| `shapeExtraSmallRadius` | `extraSmall` |
| `shapeSmallRadius` | `small` |
| `shapeMediumRadius` | `medium` |
| `shapeLargeRadius` | `large` |
| `shapeExtraLargeRadius` | `extraLarge` |

```json
"theme": { "shapeSmallRadius": 8, "shapeMediumRadius": 16, "shapeLargeRadius": 24 }
```

---

## Typography

Overrides the font size of any Material 3 type-scale role (sp, integer string). Other properties of each
style (weight, line height) are preserved from the host theme.

| JSON key | Typography role |
|----------|-----------------|
| `typographyDisplayLargeFontSize` | `displayLarge` |
| `typographyDisplayMediumFontSize` | `displayMedium` |
| `typographyDisplaySmallFontSize` | `displaySmall` |
| `typographyHeadlineLargeFontSize` | `headlineLarge` |
| `typographyHeadlineMediumFontSize` | `headlineMedium` |
| `typographyHeadlineSmallFontSize` | `headlineSmall` |
| `typographyTitleLargeFontSize` | `titleLarge` |
| `typographyTitleMediumFontSize` | `titleMedium` |
| `typographyTitleSmallFontSize` | `titleSmall` |
| `typographyBodyLargeFontSize` | `bodyLarge` |
| `typographyBodyMediumFontSize` | `bodyMedium` |
| `typographyBodySmallFontSize` | `bodySmall` |
| `typographyLabelLargeFontSize` | `labelLarge` |
| `typographyLabelMediumFontSize` | `labelMedium` |
| `typographyLabelSmallFontSize` | `labelSmall` |

```json
"theme": { "typographyHeadlineLargeFontSize": 30, "typographyBodyMediumFontSize": 15 }
```

---

## Color tokens vs hex

Component color props (`background`, `tint`, `color`, …) accept hex strings. Inside the theme system,
SDUI can also resolve **role tokens** (e.g. `primary`, `onSurface`) to the active scheme color, so a
custom component using `SduiThemeResolver.resolveColor("primary", scheme)` follows the resolved theme.
For server JSON, prefer explicit hex unless you are writing a custom component.

---

## Full themed screen

```json
{
  "screenId": "promotions",
  "schemaVersion": "1.0.0",
  "theme": {
    "primaryColor": "#FFD0BCFF",
    "onPrimaryColor": "#FF381E72",
    "backgroundColor": "#FF0F0D13",
    "surfaceColor": "#FF0F0D13",
    "onSurfaceColor": "#FFE6E1E9",
    "outlineColor": "#FF938F99",
    "shapeMediumRadius": 16,
    "shapeLargeRadius": 24,
    "shapeExtraLargeRadius": 28
  },
  "root": { "type": "Column", "children": [] }
}
```

---

## Recommendation

- Keep brand colors in the host `MaterialTheme`; only override per-screen when a campaign or screen needs
  a distinct look.
- Always provide the matching `on*` color when overriding a container/surface color, to preserve contrast.
