# SDUI — Server-Driven UI for Compose Multiplatform

Render native [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) UI dynamically
from server-provided JSON. Ship layout, styling, content, and navigation changes **without an app store
release**.

SDUI is a publishable Kotlin Multiplatform library targeting **Android**, **iOS**, and **Desktop (JVM)**.
It maps untyped JSON into a compile-time type-safe node tree, validates it before rendering, and falls
back to a native error screen instead of crashing the host app.

---

## Table of contents

- [Why SDUI](#why-sdui)
- [Features](#features)
- [Supported platforms](#supported-platforms)
- [Installation](#installation)
- [Quick start](#quick-start)
- [The JSON contract](#the-json-contract)
- [Component catalog](#component-catalog)
- [Modifiers](#modifiers)
- [Styling](#styling)
- [Actions](#actions)
- [State & template variables](#state--template-variables)
- [Theming](#theming)
- [Custom components](#custom-components)
- [Security](#security)
- [Project structure](#project-structure)
- [Building & testing](#building--testing)
- [Documentation](#documentation)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## Why SDUI

Native release cycles are slow. App-store review, staged rollouts, and user update lag mean a UI change
can take days or weeks to reach everyone. SDUI moves the **layout, content, and flow** of selected
screens to the server, so you can:

- Run promotions, banners, and campaigns that update instantly.
- A/B test layouts without shipping a binary.
- Fix UI/content bugs server-side.
- Keep one UI contract across Android, iOS, and Desktop.

What stays **native and in your control**: the rendering engine, the component implementations, the theme,
security, and any custom components you register.

---

## Features

- **Cross-platform** — one engine for Android, iOS, and Desktop (JVM) via Compose Multiplatform.
- **Type-safe domain tree** — JSON is mapped into a sealed `SduiNode` hierarchy, not untyped prop maps.
- **Schema validation** — `schemaVersion` and structure are validated before rendering; incompatible or
  malformed payloads show a native fallback screen instead of crashing.
- **Rich component catalog** — layouts, text, images, icons, inputs, lists/grids, progress, conditional
  rendering, and Material 3 components.
- **Full action pipeline** — navigation, state updates, API calls, dialogs, snackbars, analytics tracking,
  sequences, and host-app custom actions.
- **Reactive state** — per-screen `SduiStateHolder` with `{{template}}` variable resolution.
- **Server-driven theming** — full Material 3 color scheme, shapes, and typography overrides per screen.
- **Custom components** — register your own composables by type name from the host app.
- **Memory-only config** — base URLs, tokens, and headers are never serialized, logged, or persisted.

---

## Supported platforms

| Target | Status | Ktor engine |
|--------|--------|-------------|
| Android | ✅ | OkHttp |
| iOS (arm64, simulator arm64) | ✅ | Darwin |
| Desktop (JVM) | ✅ | OkHttp |

Requirements: Kotlin 2.4.0+, Compose Multiplatform 1.11+, JDK 17+, Android `minSdk` 30.

---

## Installation

The library is published to **GitHub Packages** under the coordinates `in.shanudevcodes:sdui`.

> Note: a Maven Central release is on the roadmap. Until then, configure the GitHub Packages repository.

### 1. Add the repository

In your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven {
            url = uri("https://maven.pkg.github.com/shanudeveloper/sdui")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.token").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

### 2. Add the dependency

In your shared module's `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("in.shanudevcodes:sdui:0.1.0")
        }
    }
}
```

KMP automatically resolves the correct platform artifact (`sdui-android`, `sdui-jvm`, `sdui-iosarm64`, …).

### 3. Provide an image loader (Coil)

SDUI renders network images and SVG icons through [Coil 3](https://coil-kt.github.io/coil/). Set up the
singleton image loader once in your app's root composable:

```kotlin
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder

setSingletonImageLoaderFactory { context ->
    ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory())
            add(SvgDecoder.Factory())
        }
        .build()
}
```

---

## Quick start

A minimal host screen that initializes the engine and renders a server-driven screen:

```kotlin
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import `in`.shanudevcodes.sdui.core.engine.SduiConfig
import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.core.presentation.SduiNavDisplay
import `in`.shanudevcodes.sdui.core.presentation.SduiRoute
import `in`.shanudevcodes.sdui.feature.screen.data.ScreenRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun App() {
    // 1. Initialize the engine once with your backend config (memory-only).
    remember {
        SduiEngine.initialize(
            config = SduiConfig(
                baseUrl = "https://api.example.com/",
                defaultHeaders = mapOf("Authorization" to "Bearer <token>")
            )
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val repository = remember { ScreenRepositoryImpl() }

    // 2. Build a navigation back stack with a start route.
    val backStack = remember { NavBackStack<SduiRoute>(SduiRoute.Screen("home")) }

    MaterialTheme {
        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { _ ->
            // 3. Render the current screen and wire up navigation + snackbars.
            SduiNavDisplay(
                backStack = backStack,
                repository = repository,
                onNavigate = { route, params ->
                    scope.launch {
                        if (route == "back") {
                            if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
                        } else {
                            backStack.add(SduiRoute.Screen(route, params))
                        }
                    }
                },
                onShowSnackbar = { message ->
                    scope.launch { snackbarHostState.showSnackbar(message) }
                },
                modifier = Modifier
            )
        }
    }
}
```

The engine fetches `{baseUrl}sdui/screens/{screenId}` (e.g. `https://api.example.com/sdui/screens/home`),
validates the payload, and renders it.

---

## The JSON contract

A screen is a single JSON document. Top-level shape:

```json
{
  "screenId": "home",
  "schemaVersion": "1.0.0",
  "title": "Home",
  "initialState": { "counter": 0 },
  "theme": { "primaryColor": "#FF6200EE" },
  "root": {
    "type": "Column",
    "modifiers": [ { "type": "fillMaxSize" }, { "type": "padding", "value": 16 } ],
    "props": { "verticalArrangement": "Center", "horizontalAlignment": "CenterHorizontally" },
    "children": [
      { "type": "Text", "props": { "text": "Hello, world!", "style": { "fontSize": 24, "fontWeight": "Bold" } } },
      { "type": "Spacer", "modifiers": [ { "type": "height", "value": 12 } ] },
      {
        "type": "Button",
        "props": { "text": "Tap me", "variant": "filled" },
        "modifiers": [ { "type": "clickable", "value": { "type": "ShowSnackbar", "message": "Tapped!" } } ]
      }
    ]
  }
}
```

| Field | Required | Description |
|-------|----------|-------------|
| `screenId` | ✅ | Unique screen identifier. |
| `schemaVersion` | — | Defaults to `"1.0.0"`. Validated against the client's max supported version. |
| `title` | — | Optional human-readable title. |
| `initialState` | — | Seeds the per-screen `SduiStateHolder`. |
| `theme` | — | Material 3 theme overrides (see [Theming](#theming)). |
| `root` | ✅ | The root component node. |

Every **component node** has:

```json
{ "type": "Text", "props": { /* ... */ }, "modifiers": [ /* ... */ ], "children": [ /* ... */ ], "actions": { /* ... */ } }
```

---

## Component catalog

### Layout
| `type` | Notable props |
|--------|---------------|
| `Column` | `verticalArrangement`, `horizontalAlignment`, `space`, `scrollable` |
| `Row` | `horizontalArrangement`, `verticalAlignment`, `space`, `scrollable` |
| `Box` | `contentAlignment` |
| `Spacer` | — (size via modifiers) |
| `LazyColumn` | `space` |
| `LazyRow` | `space` |
| `LazyGrid` | `columns`, `space` |
| `Scaffold` | `topBarTitle`, `topBarNavigationIcon`/`topBarNavigationIconUrl` |

### Display
| `type` | Notable props |
|--------|---------------|
| `Text` | `text`, `style` (see [Styling](#styling)) |
| `Image` | `url`, `contentDescription`, `contentScale` |
| `Icon` | `url` (SVG/PNG URL), `tint`, `name` (content description) |
| `Divider` | `thickness`, `color` |
| `Card` | `containerColor`, `shape`, `elevation`, `border` |
| `Surface` | `color`, `shape`, `elevation`, `border` |

> **Icons use URLs.** Point `url` at a Material Symbols SVG, e.g.
> `https://fonts.gstatic.com/s/i/materialiconsoutlined/search/v1/24px.svg`. The `name` prop is used only as
> the accessibility content description.

### Input
| `type` | Notable props |
|--------|---------------|
| `Button` | `text`, `variant` (`filled`, `outlined`, `text`, `elevated`, `tonal`), `enabled` |
| `IconButton` | `url`, `contentDescription` |
| `TextField` | `stateKey`, `label`, `placeholder`, `supportingText`, `isError`, `isPassword` |
| `Switch` | `stateKey`, `label` |
| `Checkbox` | `stateKey`, `label` |
| `RadioButton` | `stateKey`, `value` |
| `Slider` | `stateKey`, `valueRangeMin`, `valueRangeMax`, `steps` |
| `DropdownMenu` | `stateKey`, `options`, `label` |

### Progress & conditional
| `type` | Notable props |
|--------|---------------|
| `CircularProgress` | `color`, `size` |
| `LinearProgress` | `progress` (0.0–1.0), `color` |
| `Conditional` | `stateKey`, `operator` (`eq`, `neq`, `gt`, `lt`, `contains`, `isEmpty`, `isNotEmpty`), `value` |
| `Visible` | `stateKey`, `visible` |

---

## Modifiers

Modifiers are an **ordered list** chained into a single Compose `Modifier`.

```json
"modifiers": [
  { "type": "fillMaxWidth" },
  { "type": "padding", "value": { "horizontal": 16, "vertical": 8 } },
  { "type": "background", "value": "#FF2A2730" },
  { "type": "clip", "value": { "shape": "RoundedCorner", "radius": 12 } }
]
```

| Category | Types |
|----------|-------|
| Sizing | `width`, `height`, `size`, `fillMaxWidth`, `fillMaxHeight`, `fillMaxSize`, `wrapContentWidth`, `wrapContentHeight`, `weight`, `aspectRatio` |
| Spacing | `padding` (`all`, or `horizontal`/`vertical`, or `start`/`top`/`end`/`bottom`) |
| Visual | `background`, `backgroundGradient`, `border`, `clip`, `shadow`, `alpha` |
| Interaction | `clickable` (value = an action), `scrollable` |
| Accessibility | `testTag`, `semantics`, `semanticsButton` |
| Window insets | `statusBarsPadding`, `navigationBarsPadding`, `systemBarsPadding`, `imePadding`, `safeDrawingPadding`, `safeContentPadding`, `captionBarPadding`, `displayCutoutPadding`, `systemGesturesPadding`, `waterfallPadding` |

`weight` is scope-aware and only applies inside `Row`/`Column`.

---

## Styling

The `style` object on `Text` (and other text-bearing components):

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

| Property | Values |
|----------|--------|
| `fontWeight` | `Thin`, `Light`, `Normal`, `Medium`, `SemiBold`, `Bold`, `ExtraBold`, `Black` |
| `fontStyle` | `Normal`, `Italic` |
| `color` | Hex `#RRGGBB` or `#AARRGGBB` |
| `textAlign` | `Start`, `End`, `Center`, `Justify` |
| `overflow` | `Clip`, `Ellipsis`, `Visible` |
| `textDecoration` | `None`, `Underline`, `LineThrough` |

---

## Actions

Actions fire from `clickable` modifiers or component `actions` maps.

| Action `type` | Props |
|---------------|-------|
| `Navigate` | `route`, `params` |
| `GoBack` | — |
| `Replace` | `route`, `params` |
| `UpdateState` | `stateKey`, `value` |
| `ApiCall` | `endpoint`, `method`, `body`, `onSuccess`, `onError` |
| `ShowSnackbar` | `message`, `actionLabel`, `onAction` |
| `ShowDialog` | `title`, `message`, `confirmText`, `dismissText`, `onConfirm`, `onDismiss` |
| `Sequence` | `actions` (ordered list) |
| `Track` | `eventName`, `params` (non-visual; routed to your analytics hook) |
| `Custom` | `name`, `payload` (routed to your `actionInterceptor`) |

Example — submit a form then navigate or show an error:

```json
{
  "type": "Sequence",
  "actions": [
    { "type": "UpdateState", "stateKey": "isLoading", "value": true },
    {
      "type": "ApiCall",
      "endpoint": "/api/submit",
      "method": "POST",
      "body": { "email": "{{email_input}}" },
      "onSuccess": { "type": "Navigate", "route": "success" },
      "onError": { "type": "ShowSnackbar", "message": "Submission failed." }
    }
  ]
}
```

### Intercepting actions in the host app

Provide an `actionInterceptor` in `SduiConfig`. Return `true` to mark the action handled (the engine skips
its default behavior), or `false` to let the engine handle it:

```kotlin
SduiConfig(
    baseUrl = "https://api.example.com/",
    actionInterceptor = { action ->
        if (action.type == "Custom" && action.name == "openNativeCheckout") {
            navigateToNativeCheckout(action.payload)
            true   // handled
        } else {
            false  // let the engine handle it
        }
    }
)
```

---

## State & template variables

Each screen owns an `SduiStateHolder` (a reactive `Map<String, JsonElement>`). Input components bind to a
`stateKey`; any string prop can reference state with `{{stateKey}}`:

```json
{ "type": "Text", "props": { "text": "Hello, {{user_name}}! You have {{item_count}} items." } }
```

Seed values via `initialState`. Navigation `params` are injected as state in the destination screen, so
`Navigate` with `params: { "productId": "123" }` makes `{{productId}}` available there.

---

## Theming

By default SDUI inherits the host `MaterialTheme`. A screen can override the full Material 3 scheme via the
`theme` map (29 color roles, shape radii, and typography):

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

---

## Custom components

Register a native composable by type name from the host app, then reference it from JSON. Use the provided
extension helpers (`stringProp`, `intProp`, `booleanProp`, `action`) to read props:

```kotlin
import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.schema.action
import `in`.shanudevcodes.sdui.core.schema.stringProp

SduiEngine.registerComponent("SponsorBanner") { component, modifier, stateHolder ->
    val onAction = LocalSduiActionHandler.current
    SponsorBannerView(
        imageUrl = component.stringProp("imageUrl"),
        ctaText = component.stringProp("ctaText"),
        onClick = { component.action("onClick")?.let(onAction) },
        modifier = modifier
    )
}
```

Then in JSON:

```json
{ "type": "SponsorBanner", "props": { "imageUrl": "https://…", "ctaText": "Shop now" },
  "actions": { "onClick": { "type": "Navigate", "route": "shop" } } }
```

Registering a type that matches a built-in name overrides the built-in.

---

## Security

SDUI follows a memory-only configuration model:

- `SduiConfig` (base URL, headers, tokens) is **never** serialized, written to disk, logged, or sent in
  request bodies. Its `toString()` is redacted.
- Authentication is passed as **HTTP headers** via `defaultHeaders` or `requestInterceptor`, never in JSON
  bodies or query params.
- Recursion depth is capped (≤ 50) in both the validator and renderer to mitigate malicious payloads.
- Invalid or version-incompatible payloads render a native `FallbackScreen` rather than crashing.

> When exposing any network endpoint or embedding tokens, ensure transport is HTTPS in production.
> Additional guardrails (HTTPS enforcement, response-size caps, per-field validation) are on the roadmap.

---

## Project structure

This repository is the SDUI library **plus** sample apps that demonstrate it:

| Module | Purpose |
|--------|---------|
| `:sdui` | The publishable KMP engine (`core/`, `feature/`, `navigation/`). No hardcoded URLs, no demo code. |
| `:shared` | Demo shared Compose UI (`App.kt`, `SduiBannerCard.kt`). Depends on and re-exports `:sdui`. |
| `:androidApp` | Android sample app entry point. |
| `:desktopApp` | Desktop (JVM) sample app entry point. |
| `:iosApp` | iOS sample app (SwiftUI host). |
| `server/` | Sample screen JSON served for local testing. |

---

## Building & testing

```bash
# Library unit/UI tests (the engine — 125 tests)
./gradlew :sdui:jvmTest

# iOS library tests (macOS only)
./gradlew :sdui:iosSimulatorArm64Test

# Sample apps
./gradlew :androidApp:assembleDebug
./gradlew :desktopApp:run            # or :desktopApp:hotRun --auto for hot reload
```

`:shared:jvmTest` runs the demo module's (trivial) tests — engine coverage lives in `:sdui`.

### Trying it locally

Serve the sample screens and point a sample app at them:

```bash
python -m http.server 8085 --directory server
```

Then set `baseUrl` to your machine's address (the included demo uses `http://<host>:8085/`) and run a
sample app. Screens resolve from `server/sdui/screens/<screenId>.json`.

---

## Documentation

For in-depth, developer-focused documentation, see the **[developer guide](./docs/guide/00-INDEX.md)** —
getting started, full JSON schema reference, component/modifier/action references, theming, custom
components, navigation, security, troubleshooting, and a recipes cookbook.

---

## Roadmap

- Offline caching (SQLDelight) + local asset fallback templates
- Host-app feature flags in `SduiConfig` for conditional layouts
- Maven Central publication
- Security guardrails: HTTPS enforcement, response-size cap, per-field validation
- Binary Compatibility Validator (`.api` dumps)

---

## Contributing

See [docs/CONTRIBUTING.md](./docs/CONTRIBUTING.md) for the architecture rules, file-atomicity conventions,
testing requirements, and PR checklist. In short: Clean Architecture (`presentation → domain ← data`), one
declaration per file, no DTO/Ktor imports in `presentation/`, and tests for all new or changed code.

---

## License

Apache License 2.0.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).
