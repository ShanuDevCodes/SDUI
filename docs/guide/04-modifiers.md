# 04 — Modifiers

Modifiers are an **ordered list** on each node. `ModifierResolver` chains them, in order, into a single
Compose `Modifier`. Order matters — e.g. `padding` then `background` differs from `background` then `padding`.

```json
"modifiers": [
  { "type": "fillMaxWidth" },
  { "type": "padding", "value": { "horizontal": 16, "vertical": 8 } },
  { "type": "background", "value": "#FF2A2730" },
  { "type": "clip", "value": { "shape": "RoundedCorner", "radius": 12 } }
]
```

Unknown modifier `type`s are skipped gracefully.

---

## Sizing

| Type | `value` shape | Result |
|------|---------------|--------|
| `width` | int (dp) | `Modifier.width(value.dp)` |
| `height` | int (dp) | `Modifier.height(value.dp)` |
| `size` | int (dp), or `{ "width": w, "height": h }` | `Modifier.size(...)` |
| `fillMaxWidth` | float fraction (default `1.0`) | `Modifier.fillMaxWidth(fraction)` |
| `fillMaxHeight` | float fraction (default `1.0`) | `Modifier.fillMaxHeight(fraction)` |
| `fillMaxSize` | float fraction (default `1.0`) | `Modifier.fillMaxSize(fraction)` |
| `wrapContentWidth` | — | `Modifier.wrapContentWidth()` |
| `wrapContentHeight` | — | `Modifier.wrapContentHeight()` |
| `aspectRatio` | float | `Modifier.aspectRatio(ratio)` |
| `weight` | float | **Scope-aware** — only applied inside `Row`/`Column`. Ignored elsewhere. |

```json
{ "type": "size", "value": 44 }
{ "type": "size", "value": { "width": 120, "height": 48 } }
{ "type": "fillMaxWidth", "value": 0.5 }
{ "type": "weight", "value": 1.0 }
```

---

## Spacing

### `padding`
Three accepted `value` shapes:

```json
{ "type": "padding", "value": 16 }
{ "type": "padding", "value": { "horizontal": 16, "vertical": 8 } }
{ "type": "padding", "value": { "start": 8, "top": 4, "end": 8, "bottom": 4 } }
```

| Form | Behavior |
|------|----------|
| number | Uniform padding on all sides. |
| `{ "all": n }` | Uniform padding. |
| `{ "horizontal", "vertical" }` | Symmetric padding. |
| `{ "start", "top", "end", "bottom" }` | Per-edge (missing edges = 0). |

---

## Visual

| Type | `value` shape | Notes |
|------|---------------|-------|
| `background` | hex string | Solid background color. |
| `backgroundGradient` | `{ "colors": ["#..","#.."], "direction": "vertical" }` | `direction`: `vertical` (default), `horizontal`, `diagonal`. Needs ≥ 2 colors. |
| `border` | `{ "width": 1, "color": "#..", "shape": ... }` | Outlined border. |
| `clip` | shape (see below) | Clips to a shape. |
| `shadow` | int (dp), or `{ "elevation": n, "shape": ... }` | Drop shadow. |
| `alpha` | float (0.0–1.0) | Opacity. |

```json
{ "type": "background", "value": "#FF2A2730" }
{ "type": "backgroundGradient", "value": { "colors": ["#FF1A0B2E", "#FF0F0D13"], "direction": "vertical" } }
{ "type": "border", "value": { "width": 1, "color": "#FF938F99", "shape": { "shape": "RoundedCorner", "radius": 12 } } }
{ "type": "shadow", "value": { "elevation": 8, "shape": { "shape": "RoundedCorner", "radius": 16 } } }
```

### Shape values
Used by `clip`, `border.shape`, `shadow.shape`:

```json
"Circle"
{ "shape": "Circle" }
{ "shape": "RoundedCorner", "radius": 12 }
```

Anything else resolves to a rectangle.

---

## Interaction

### `clickable`
`value` is an **action object** (see [05 — Actions & state](./05-actions-and-state.md)).

```json
{ "type": "clickable", "value": { "type": "Navigate", "route": "detail", "params": { "id": "7" } } }
```

### `scrollable`
Marks a `Column`/`Row` scrollable. In practice prefer the `scrollable` **prop** on `Column`/`Row`
(the renderers handle scroll there); the modifier form is a no-op placeholder in the global chain.

---

## Accessibility

| Type | `value` shape | Result |
|------|---------------|--------|
| `testTag` | string | `Modifier.testTag(tag)` — for UI tests. |
| `semantics` | `{ "contentDescription": "..." }` | Sets content description. |
| `semanticsButton` | — | Marks the node with `Role.Button`. |

```json
{ "type": "testTag", "value": "submit_button" }
{ "type": "semantics", "value": { "contentDescription": "Submit the form" } }
```

---

## Window insets

No `value` needed. Each maps to the corresponding Compose `WindowInsets` padding:

| Type |
|------|
| `statusBarsPadding` |
| `navigationBarsPadding` |
| `systemBarsPadding` |
| `imePadding` |
| `safeDrawingPadding` |
| `safeContentPadding` |
| `captionBarPadding` |
| `displayCutoutPadding` |
| `systemGesturesPadding` |
| `waterfallPadding` |

```json
{ "type": "statusBarsPadding" }
```

---

## Box child alignment

Inside a `Column`, children may use an `align` modifier (`Start`/`CenterHorizontally`/`End`).
Inside a `Box`, set `contentAlignment` on the `Box` prop instead (see [03 — Components](./03-components.md)).
