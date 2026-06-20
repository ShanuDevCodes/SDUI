# 08 — Navigation

SDUI uses [Navigation 3](https://developer.android.com/guide/navigation). The host app owns a
`NavBackStack<SduiRoute>` and supplies navigation callbacks; `SduiNavDisplay` renders the top of the stack
and creates a fresh screen ViewModel per route.

---

## The pieces

| Type | Package | Role |
|------|---------|------|
| `SduiRoute` | `in.shanudevcodes.sdui.core.presentation` | Sealed route key. `SduiRoute.Screen(screenId, params)`. |
| `SduiNavDisplay` | `in.shanudevcodes.sdui.core.presentation` | Composable Nav3 display container. |
| `ScreenRepositoryImpl` | `in.shanudevcodes.sdui.feature.screen.data` | Default repository fetching screens via the engine. |
| `NavBackStack` | `androidx.navigation3.runtime` | The mutable back stack you own. |

---

## SduiRoute

```kotlin
@Serializable
sealed interface SduiRoute : NavKey {
    @Serializable
    data class Screen(
        val screenId: String,
        val params: Map<String, String> = emptyMap()
    ) : SduiRoute
}
```

- `screenId` — which screen to fetch (`GET {baseUrl}sdui/screens/{screenId}`).
- `params` — injected as state in the destination screen, so `{{paramKey}}` resolves there.

---

## Wiring SduiNavDisplay

```kotlin
val backStack = remember { NavBackStack<SduiRoute>(SduiRoute.Screen("home")) }
val repository = remember { ScreenRepositoryImpl() }

SduiNavDisplay(
    backStack = backStack,
    repository = repository,
    onNavigate = { route, params -> /* push or pop */ },
    onShowSnackbar = { message -> /* show snackbar */ },
    modifier = Modifier
)
```

| Parameter | Purpose |
|-----------|---------|
| `backStack` | Your `NavBackStack<SduiRoute>`. The display renders the top entry. |
| `repository` | A `ScreenRepository` (use `ScreenRepositoryImpl`). |
| `onNavigate` | Called for `Navigate`, `GoBack` (as route `"back"`), and `Replace`. You mutate the stack. |
| `onShowSnackbar` | Called for `ShowSnackbar` actions. |

---

## Handling navigation

The engine funnels navigation through `onNavigate(route, params)`. The convention used by the sample app:

```kotlin
onNavigate = { route, params ->
    scope.launch {
        if (route == "back") {
            if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
        } else {
            backStack.add(SduiRoute.Screen(route, params))
        }
    }
}
```

- `Navigate` → `onNavigate(route, params)` → push `SduiRoute.Screen(route, params)`.
- `GoBack` → `onNavigate("back", emptyMap())` → pop the top entry.
- `Replace` → `onNavigate(route, params)` in the default flow (treat as push, or implement true replace by
  popping then pushing).

> The string `"back"` is the engine's sentinel for a back/pop request. Always guard against popping the
> root (`if (backStack.size > 1)`).

### True replace

If you want `Replace` to behave distinctly from `Navigate`, branch on a sentinel or maintain your own
mapping. Since both arrive via `onNavigate`, one approach is to encode intent in the route or wrap
`SduiNavDisplay` with your own logic that pops the current entry before pushing.

---

## Passing data between screens

```json
{ "type": "Navigate", "route": "product", "params": { "productId": "123", "source": "home" } }
```

In the `product` screen, the params are seeded into state, so:

```json
{ "type": "Text", "props": { "text": "Product #{{productId}} (from {{source}})" } }
```

renders `Product #123 (from home)`.

---

## Deep links and tab navigation

`SduiNavBackStackExtensions` provides helpers on `NavBackStack<SduiRoute>`:

| Function | Purpose |
|----------|---------|
| `isTopAsState(route): State<Boolean>` | Observe whether a route is currently on top (e.g. to highlight a selected tab). |
| `navigateTab(route)` | Switch to a base/tab route, clearing intermediate screens. |

```kotlin
import `in`.shanudevcodes.sdui.core.presentation.navigateTab
import `in`.shanudevcodes.sdui.core.presentation.isTopAsState

val isHomeSelected by backStack.isTopAsState(SduiRoute.Screen("home"))
// ...
backStack.navigateTab(SduiRoute.Screen("home"))
```

For `DeepLink` actions, handle them in your `actionInterceptor` and translate the URL into a stack mutation
(see [05 — Actions & state](./05-actions-and-state.md)).

---

## Lifecycle notes

- A new `SduiScreenViewModel` is created per `screenId` when its entry is first shown; it fetches and
  validates the screen, then renders. Returning to an existing entry reuses its retained state.
- Each screen has its own `SduiStateHolder`, seeded from `initialState` + route `params`.
- While fetching, a centered `CircularProgressIndicator` is shown. On failure, a `FallbackScreen` with a
  retry button is shown (see [10 — Troubleshooting](./10-troubleshooting.md)).
