# 05 — Actions & state

Actions are how server-driven UI does things: navigate, mutate state, call APIs, show feedback. State is a
per-screen reactive key-value store that input components bind to and templates read from.

---

## Where actions come from

1. A `clickable` modifier — `value` is an action object.
2. A component's `actions` map — named entries (e.g. `onClick`) read by custom renderers via `action("onClick")`.
3. Follow-ups inside other actions — `onSuccess`, `onError`, `onConfirm`, `onDismiss`, `Sequence.actions`.

```json
{ "type": "clickable", "value": { "type": "ShowSnackbar", "message": "Saved" } }
```

---

## Action catalog

| `type` | Fields | Behavior |
|--------|--------|----------|
| `Navigate` | `route`, `params` | Push a new screen (host wires the back stack). |
| `GoBack` | — | Pop the current screen. |
| `Replace` | `route`, `params` | Replace the current screen. |
| `PopToRoot` | — | Pop to the root screen.¹ |
| `DeepLink` | `url` | Hand a deep-link URL to the host.¹ |
| `UpdateState` | `stateKey`, `value` | Set a state value. |
| `ApiCall` | `endpoint`, `method`, `body`, `onSuccess`, `onError` | HTTP call with follow-up actions. |
| `ShowSnackbar` | `message`, `actionLabel`, `onAction` | Show a snackbar. |
| `ShowDialog` | `title`, `message`, `confirmText`, `dismissText`, `onConfirm`, `onDismiss` | Show an alert dialog. |
| `Sequence` | `actions` | Run actions in order. |
| `Conditional` | `stateKey`, `operator`, `value`/`compareValue`, `thenAction`, `elseAction` | Branch on a condition. |
| `Custom` | `name`, `payload` | Host-defined action.² |
| `Track` | `eventName`, `params` | Analytics event.² |

> ¹ `PopToRoot` and `DeepLink` require the host to supply the corresponding callbacks. The default
> `SduiNavDisplay` flow wires `Navigate`, `GoBack`, `Replace`, `ShowSnackbar`, `ShowDialog`, and `ApiCall`.
> For `PopToRoot`/`DeepLink`, handle them via the `actionInterceptor` (below) or your own back-stack logic.
>
> ² `Custom` and `Track` are best handled through the `actionInterceptor` in `SduiConfig`, which sees
> **every** action before default handling. The default screen flow does not wire dedicated
> custom/analytics callbacks, so the interceptor is the supported integration point.

---

## Intercepting actions (host integration)

`SduiConfig.actionInterceptor` is invoked for **every** action before the engine handles it. Return `true`
to consume the action (engine skips its default), `false` to let the engine proceed.

```kotlin
SduiConfig(
    baseUrl = "https://api.example.com/",
    actionInterceptor = { action ->
        when {
            action.type == "Track" -> {
                analytics.log(action.eventName ?: "", action.payload)
                true  // consumed
            }
            action.type == "Custom" && action.name == "openCheckout" -> {
                openNativeCheckout(action.payload)
                true
            }
            action.type == "DeepLink" -> {
                router.handle(action.url ?: "")
                true
            }
            else -> false  // engine handles Navigate, ApiCall, dialogs, etc.
        }
    }
)
```

The action passed to the interceptor is a `SduiActionDto` exposing `type`, `route`, `params`, `url`,
`stateKey`, `value`, `endpoint`, `method`, `body`, `message`, `name`, `payload`, `eventName`, and the
nested follow-up actions.

---

## `UpdateState`

```json
{ "type": "UpdateState", "stateKey": "isLoading", "value": true }
```

Sets `isLoading` in the screen's `SduiStateHolder`. Any component bound to that key (or template
referencing it) updates reactively.

---

## `Sequence`

Run multiple actions in order (e.g. set loading, then call API):

```json
{
  "type": "Sequence",
  "actions": [
    { "type": "UpdateState", "stateKey": "isLoading", "value": true },
    {
      "type": "ApiCall",
      "endpoint": "/api/submit",
      "method": "POST",
      "body": { "email": "{{email}}" },
      "onSuccess": { "type": "Navigate", "route": "success" },
      "onError": { "type": "ShowSnackbar", "message": "Failed. Try again." }
    }
  ]
}
```

---

## `ApiCall`

| Field | Notes |
|-------|-------|
| `endpoint` | Path appended to `baseUrl` (relative). Required. |
| `method` | HTTP method string (default `POST`). |
| `body` | JSON body. `{{template}}` placeholders are resolved against state before sending. |
| `onSuccess` | Action dispatched on a 2xx response. |
| `onError` | Action dispatched on non-2xx or exception. |

The request uses the engine's configured `HttpClient` (with your `defaultHeaders` and `requestInterceptor`).
The body is sent as `application/json`. The response body is **not** parsed into UI automatically — drive
UI changes via `onSuccess`/`onError` follow-up actions (e.g. `Navigate`, `UpdateState`).

```json
{
  "type": "ApiCall",
  "endpoint": "/api/cart/add",
  "method": "POST",
  "body": { "productId": "{{productId}}", "qty": 1 },
  "onSuccess": { "type": "ShowSnackbar", "message": "Added to cart" },
  "onError": { "type": "ShowSnackbar", "message": "Could not add to cart" }
}
```

---

## `ShowDialog`

```json
{
  "type": "ShowDialog",
  "title": "Delete item?",
  "message": "This cannot be undone.",
  "confirmText": "Delete",
  "dismissText": "Cancel",
  "onConfirm": { "type": "ApiCall", "endpoint": "/api/delete", "method": "POST" },
  "onDismiss": { "type": "Track", "eventName": "delete_cancelled" }
}
```

`confirmText` defaults to `OK`. If `dismissText` is omitted, no dismiss button is shown.

---

## `Conditional` action

Branches at dispatch time based on state:

```json
{
  "type": "Conditional",
  "stateKey": "agreed",
  "operator": "eq",
  "value": true,
  "thenAction": { "type": "Navigate", "route": "checkout" },
  "elseAction": { "type": "ShowSnackbar", "message": "Please accept the terms" }
}
```

---

## State management

### `SduiStateHolder`
Each screen owns one. It wraps a reactive `Map<String, JsonElement>`:

- Seeded from the screen's `initialState`.
- Seeded with navigation `params` from the route.
- Mutated by `UpdateState` actions and input components bound via `stateKey`.

### Input binding
`TextField`, `Switch`, `Checkbox`, `RadioButton`, `Slider`, and `DropdownMenu` accept a `stateKey` and
read/write that key two-way. Example: a `Switch` with `stateKey: "notifications"` toggles the boolean
`notifications` in state.

### Template variables
Any string prop can interpolate state with `{{key}}`:

```json
{ "type": "Text", "props": { "text": "Hi {{userName}}, you have {{count}} messages" } }
```

Templates are resolved at render time, so they update live when state changes. They also work inside
`ApiCall.body`.

### Passing data between screens
`Navigate` params become state keys in the destination:

```json
{ "type": "Navigate", "route": "product", "params": { "productId": "123" } }
```

In `product`, `{{productId}}` resolves to `"123"`.

---

## Condition operator semantics

Used by both the `Conditional` component and `Conditional` action (`ConditionEvaluator`):

| Operator | Meaning |
|----------|---------|
| (empty) | True if the state value is boolean `true`. |
| `eq` | Equal (with boolean & numeric coercion). |
| `neq` | Not equal. |
| `gt` | Numeric greater-than. |
| `lt` | Numeric less-than. |
| `contains` | String contains substring. |
| `isEmpty` | Null or empty string. |
| `isNotEmpty` | Non-empty. |

`eq`/`neq` coerce types: `"true"` equals `true`, `1` equals `1.0`, otherwise content strings are compared.
`gt`/`lt` compare numeric values (non-numeric → `0`).
