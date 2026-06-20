# 02 — JSON schema reference

This page is the complete field-by-field reference for the JSON a server sends to SDUI. The engine
deserializes this into DTOs, validates them, then maps them into the type-safe `SduiNode` domain tree
before rendering.

---

## Screen document

The top-level object returned by `GET {baseUrl}sdui/screens/{screenId}`.

```json
{
  "screenId": "home",
  "schemaVersion": "1.0.0",
  "title": "Home",
  "initialState": { "counter": 0, "agreed": false },
  "theme": { "primaryColor": "#FF6200EE" },
  "root": { "type": "Column", "children": [] }
}
```

| Field | Type | Required | Default | Notes |
|-------|------|----------|---------|-------|
| `screenId` | string | ✅ | — | Unique identifier of the screen. |
| `schemaVersion` | string | — | `"1.0.0"` | Validated against the client max (`1.0.0`). Higher → fallback. |
| `title` | string | — | `null` | Optional human-readable title. |
| `initialState` | object | — | `{}` | Seeds the per-screen `SduiStateHolder`. Values are raw JSON. |
| `theme` | object (string→string) | — | `{}` | Material 3 overrides. See [06 — Theming](./06-theming.md). |
| `root` | node | ✅ | — | The root component node. |

> **Validation rules** (`SduiValidator`):
> 1. `schemaVersion` must be ≤ `1.0.0` (semver compare, component-wise).
> 2. `root.type` must be non-blank.
> 3. Component tree depth must be ≤ `50`.
>
> If any rule fails, the screen renders a native `FallbackScreen` instead of crashing.

---

## Component node

Every node in the tree has this shape:

```json
{
  "type": "Text",
  "props": { "text": "Hello" },
  "modifiers": [ { "type": "padding", "value": 8 } ],
  "children": [],
  "actions": { "onClick": { "type": "GoBack" } }
}
```

| Field | Type | Required | Default | Notes |
|-------|------|----------|---------|-------|
| `type` | string | ✅ | — | Component type. Resolved against the `ComponentRegistry`. Unknown types are skipped with a warning. |
| `props` | object (string→JSON) | — | `{}` | Component-specific properties. See [03 — Components](./03-components.md). |
| `modifiers` | array of modifier | — | `[]` | Ordered modifier list. See [04 — Modifiers](./04-modifiers.md). |
| `children` | array of node | — | `[]` | Child nodes (for layout/container components). |
| `actions` | object (string→action) | — | `{}` | Named actions a component or custom renderer can read (e.g. `onClick`). |

### `props` value types

`props` is a map of string keys to **raw JSON values**. Renderers read them with typed helpers:

| Helper | Reads | Fallback |
|--------|-------|----------|
| `stringProp("key")` | JSON string | `""` |
| `intProp("key")` | JSON int | `0` |
| `booleanProp("key")` | JSON bool | `false` |
| `action("key")` | entry from `actions` map | `null` |

String props support `{{template}}` resolution against state — see [05 — Actions & state](./05-actions-and-state.md).

---

## Modifier object

```json
{ "type": "padding", "value": { "horizontal": 16, "vertical": 8 } }
```

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `type` | string | ✅ | Modifier name (e.g. `padding`, `fillMaxWidth`, `clickable`). |
| `value` | JSON | — | Shape depends on the modifier. May be a number, string, object, or an action. |

Unknown modifier types are skipped gracefully. See [04 — Modifiers](./04-modifiers.md) for every type and its `value` shape.

---

## Style object

Used by `Text` (the `style` prop). All fields optional.

```json
{
  "fontSize": 16,
  "fontWeight": "Bold",
  "fontStyle": "Italic",
  "color": "#FF1A1A2E",
  "textAlign": "Center",
  "letterSpacing": 0.5,
  "lineHeight": 24,
  "maxLines": 2,
  "overflow": "Ellipsis",
  "textDecoration": "Underline"
}
```

| Field | Type | Values |
|-------|------|--------|
| `fontSize` | int | sp |
| `fontWeight` | string | `Thin`, `Light`, `Normal`, `Medium`, `SemiBold`, `Bold`, `ExtraBold`, `Black` |
| `fontStyle` | string | `Normal`, `Italic` |
| `color` | string | `#RRGGBB` or `#AARRGGBB` |
| `textAlign` | string | `Start`, `End`, `Center`, `Justify` |
| `letterSpacing` | float | sp |
| `lineHeight` | int | sp |
| `maxLines` | int | line cap |
| `overflow` | string | `Clip`, `Ellipsis`, `Visible` |
| `textDecoration` | string | `None`, `Underline`, `LineThrough` |

---

## Action object

```json
{ "type": "Navigate", "route": "detail", "params": { "id": "42" } }
```

`type` selects the behavior; the other fields depend on the type. See
[05 — Actions & state](./05-actions-and-state.md) for the full action catalog and field reference.

---

## Colors

Anywhere a color is accepted (`style.color`, `background`, `border`, `tint`, theme values), the format is
a hex string:

- `#RRGGBB` — opaque (alpha forced to `FF`)
- `#AARRGGBB` — explicit alpha

Invalid or missing colors fall back to `Color.Unspecified` (i.e. inherit/transparent), never crash.

---

## Full example

```json
{
  "screenId": "profile",
  "schemaVersion": "1.0.0",
  "title": "Profile",
  "initialState": { "notifications": true },
  "theme": { "primaryColor": "#FFD0BCFF", "backgroundColor": "#FF0F0D13" },
  "root": {
    "type": "Column",
    "modifiers": [ { "type": "fillMaxSize" }, { "type": "padding", "value": 16 } ],
    "props": { "space": 12 },
    "children": [
      { "type": "Text", "props": { "text": "Welcome, {{userName}}", "style": { "fontSize": 24, "fontWeight": "Bold" } } },
      {
        "type": "Switch",
        "props": { "stateKey": "notifications", "label": "Enable notifications" }
      },
      {
        "type": "Button",
        "props": { "text": "Save", "variant": "filled" },
        "modifiers": [
          { "type": "fillMaxWidth" },
          { "type": "clickable", "value": { "type": "ShowSnackbar", "message": "Saved!" } }
        ]
      }
    ]
  }
}
```
