# 03 — Components

Every built-in component, its `props`, and a JSON example. Component `type` is resolved against the
`ComponentRegistry`; unknown types are skipped with a warning (the rest of the tree still renders).

Prop value types are read with helpers: string → `stringProp`, int → `intProp`, bool → `booleanProp`.
Missing props use the defaults listed below.

---

## Layout

### `Column`
Vertical stack.

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `space` | int (dp) | `-1` | If ≥ 0, uses `Arrangement.spacedBy(space.dp)` and overrides `verticalArrangement`. |
| `verticalArrangement` | string | `Top` | `Top`, `Center`/`CenterVertically`, `Bottom`, `SpaceBetween`, `SpaceAround`, `SpaceEvenly`. |
| `horizontalAlignment` | string | `Start` | `Start`, `Center`/`CenterHorizontally`, `End`. |
| `scrollable` | bool | `false` | Wraps in `verticalScroll`. |

Children may carry a `weight` modifier (float) and an `align` modifier (`Start`/`CenterHorizontally`/`End`).

```json
{
  "type": "Column",
  "props": { "space": 12, "horizontalAlignment": "CenterHorizontally" },
  "modifiers": [ { "type": "fillMaxSize" } ],
  "children": []
}
```

### `Row`
Horizontal stack. Mirror of `Column`:

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `space` | int (dp) | `-1` | `Arrangement.spacedBy`. |
| `horizontalArrangement` | string | `Start` | `Start`, `Center`, `End`, `SpaceBetween`, `SpaceAround`, `SpaceEvenly`. |
| `verticalAlignment` | string | `Top` | `Top`, `Center`/`CenterVertically`, `Bottom`. |
| `scrollable` | bool | `false` | Wraps in `horizontalScroll`. |

Children may carry a `weight` modifier.

### `Box`
Overlapping stack.

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `contentAlignment` | string | `TopStart` | `Center`, `TopStart`, `TopEnd`, `BottomStart`, `BottomEnd`, `CenterStart`, `CenterEnd`, `TopCenter`, `BottomCenter`. |

### `Spacer`
Empty space. No props — size it with `width`/`height`/`size` modifiers.

```json
{ "type": "Spacer", "modifiers": [ { "type": "height", "value": 16 } ] }
```

### `LazyColumn` / `LazyRow`
Lazily-rendered list of `children`.

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `space` | int (dp) | `0` | Spacing between items. |

### `LazyGrid`
Lazy vertical grid.

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `columns` | int | `2` | Fixed column count. |
| `space` | int (dp) | `0` | Vertical & horizontal spacing. |

### `Scaffold`
Material 3 scaffold with a top app bar.

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `topBarTitle` | string | `""` | App bar title. |
| `topBarNavigationIcon` / `topBarNavigationIconUrl` | string (URL) | — | Navigation icon (both prop names accepted). |

Body content comes from `children`.

---

## Display

### `Text`

| Prop | Type | Notes |
|------|------|-------|
| `text` | string | Supports `{{template}}` resolution. |
| `style` | object | See [styling](./02-json-schema-reference.md#style-object). |

```json
{ "type": "Text", "props": { "text": "Hello, {{name}}", "style": { "fontSize": 18, "fontWeight": "SemiBold" } } }
```

### `Image`
Network image via Coil `AsyncImage`.

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `url` | string | — | Image URL. |
| `contentDescription` | string | `null` | Accessibility. |
| `contentScale` | string | `Crop` | `Crop`, `Fit`, `FillBounds`, `Inside`, `None`. |

### `Icon`
SVG/PNG icon via Coil. **Icons are URLs, not names.**

| Prop | Type | Notes |
|------|------|-------|
| `url` | string | SVG/PNG URL, e.g. a Material Symbols SVG. |
| `tint` | string | Hex color. |
| `name` | string | Used only as accessibility content description. |

```json
{ "type": "Icon", "props": { "url": "https://fonts.gstatic.com/s/i/materialiconsoutlined/search/v1/24px.svg", "tint": "#FFE6E1E9" } }
```

CDN families: `materialiconsoutlined`, `materialiconsround`, `materialiconssharp`. With no size modifier,
the icon defaults to 24dp.

### `Divider`

| Prop | Type | Notes |
|------|------|-------|
| `thickness` | int (dp) | Line thickness. |
| `color` | string | Hex color. |

### `Card` / `Surface`
Containers with children.

| Prop | Type | Notes |
|------|------|-------|
| `containerColor` / `color` | string | Background color (`Card` uses `containerColor`, `Surface` uses `color`). |
| `shape` | string/object | `Circle`, or `{ "shape": "RoundedCorner", "radius": 12 }`. |
| `elevation` | int (dp) | Shadow elevation. |
| `border` | object | `{ "width": 1, "color": "#..." }`. |

A `clickable` modifier on a `Card`/`Surface` makes the whole container tappable.

---

## Input

### `Button`

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `text` | string | — | Button label. |
| `variant` | string | `filled` | `filled`, `outlined`, `text`, `elevated`, `tonal`. |
| `enabled` | bool | `true` | — |

Attach the action via a `clickable` modifier:

```json
{
  "type": "Button",
  "props": { "text": "Continue", "variant": "filled" },
  "modifiers": [ { "type": "clickable", "value": { "type": "Navigate", "route": "next" } } ]
}
```

### `IconButton`

| Prop | Type | Notes |
|------|------|-------|
| `url` | string | Icon URL. |
| `contentDescription` | string | Accessibility. |

### `TextField`
Two-way bound to a `stateKey`.

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `stateKey` | string | `""` | State key for two-way binding. Empty = local only. |
| `variant` | string | `outlined` | `outlined` or `filled`. |
| `label` | string | `""` | Supports templates. |
| `placeholder` | string | `""` | Supports templates. |
| `supportingText` | string | `""` | Helper text below the field. |
| `isError` | bool | `false` | Error styling. |
| `enabled` | bool | `true` | — |
| `singleLine` | bool | `true` | — |
| `leadingIcon` / `trailingIcon` | string (URL) | `""` | 20dp icon slots. |
| `keyboardType` | string | `Text` | `Text`, `Number`, `Email`, `Password`, `Phone`, `Uri`. |
| `visualTransformation` | string | — | `Password` to mask input. |

```json
{
  "type": "TextField",
  "props": {
    "stateKey": "email", "label": "Email", "keyboardType": "Email",
    "leadingIcon": "https://fonts.gstatic.com/s/i/materialiconsoutlined/email/v1/24px.svg"
  }
}
```

### `Switch` / `Checkbox`

| Prop | Type | Notes |
|------|------|-------|
| `stateKey` | string | Bound boolean state key. |
| `label` | string | Optional label. |

### `RadioButton`

| Prop | Type | Notes |
|------|------|-------|
| `stateKey` | string | Group state key. |
| `value` | string | This button's value; selected when state equals it. |

### `Slider`

| Prop | Type | Default | Notes |
|------|------|---------|-------|
| `stateKey` | string | `""` | Bound numeric (float) state key. |
| `valueRangeMin` | float | `0.0` | Range minimum. |
| `valueRangeMax` | float | `1.0` | Range maximum. |
| `steps` | int | `0` | Discrete steps between min and max. |
| `enabled` | bool | `true` | — |

> Note the prop names are `valueRangeMin` / `valueRangeMax` (not `min`/`max`).

```json
{ "type": "Slider", "props": { "stateKey": "volume", "valueRangeMin": 0, "valueRangeMax": 100, "steps": 9 } }
```

### `DropdownMenu`
Exposed dropdown menu.

| Prop | Type | Notes |
|------|------|-------|
| `stateKey` | string | Bound selected value. |
| `label` | string | Field label. |
| `enabled` | bool | — |
| `options` | array | Either `["A","B"]` or `[{ "label": "Free", "value": "free" }]`. |

```json
{
  "type": "DropdownMenu",
  "props": {
    "stateKey": "plan", "label": "Plan",
    "options": [ { "label": "Free", "value": "free" }, { "label": "Pro", "value": "pro" } ]
  }
}
```

---

## Progress

### `CircularProgress`

| Prop | Type | Notes |
|------|------|-------|
| `color` | string | Hex color. |
| `size` | int (dp) | Spinner diameter. |

### `LinearProgress`

| Prop | Type | Notes |
|------|------|-------|
| `progress` | float (0.0–1.0) | Omit for indeterminate. |
| `color` | string | Hex color. |

---

## Conditional

### `Conditional`
Renders its `children` only when a condition holds.

| Prop | Type | Notes |
|------|------|-------|
| `stateKey` | string | State key to read. |
| `operator` | string | `eq`, `neq`, `gt`, `lt`, `contains`, `isEmpty`, `isNotEmpty`. Empty operator = truthy boolean check. |
| `value` | JSON | Comparison value. |

```json
{
  "type": "Conditional",
  "props": { "stateKey": "cartCount", "operator": "gt", "value": 0 },
  "children": [ { "type": "Text", "props": { "text": "You have items in your cart" } } ]
}
```

### `Visible`
Shows/hides `children` based on a boolean state.

| Prop | Type | Notes |
|------|------|-------|
| `stateKey` | string | Boolean state key. |
| `visible` | bool | Static fallback if no state key. |

See [05 — Actions & state](./05-actions-and-state.md) for operator semantics (numeric/boolean coercion).
