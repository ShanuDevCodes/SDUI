# 01 — Getting started

This page takes you from zero to a rendered server-driven screen in your own Compose Multiplatform app.

---

## Prerequisites

- Kotlin **2.4.0+**
- Compose Multiplatform **1.11+**
- JDK **17+**
- Android `minSdk` **30** (if targeting Android)
- A Compose Multiplatform project with a shared module (Android / iOS / Desktop targets)

---

## 1. Add the GitHub Packages repository

`settings.gradle.kts`:

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

Provide credentials in `~/.gradle/gradle.properties` (never commit them):

```properties
gpr.user=your-github-username
gpr.token=ghp_yourPersonalAccessTokenWithReadPackagesScope
```

> The token needs the `read:packages` scope.

---

## 2. Add the dependency

In your shared module `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("in.shanudevcodes:sdui:0.1.0")
        }
    }
}
```

Kotlin Multiplatform automatically selects the correct platform artifact
(`sdui-android`, `sdui-jvm`, `sdui-iosarm64`, `sdui-iossimulatorarm64`).

---

## 3. Configure the Coil image loader

SDUI renders network images (`Image`) and SVG icons (`Icon`) via [Coil 3](https://coil-kt.github.io/coil/).
Register the singleton image loader **once**, high in your composition (before any SDUI screen renders):

```kotlin
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.svg.SvgDecoder

setSingletonImageLoaderFactory { context ->
    ImageLoader.Builder(context)
        .components {
            add(KtorNetworkFetcherFactory())  // network images + SVG icons
            add(SvgDecoder.Factory())         // decode SVG payloads
        }
        .build()
}
```

> Without `KtorNetworkFetcherFactory`, network images and icons silently fail to load.
> Without `SvgDecoder`, SVG icons (e.g. Material Symbols) won't decode.

---

## 4. Initialize the engine

Call `SduiEngine.initialize` exactly once before rendering. Wrap it in `remember {}` so it
runs on first composition:

```kotlin
import `in`.shanudevcodes.sdui.core.engine.SduiConfig
import `in`.shanudevcodes.sdui.core.engine.SduiEngine

remember {
    SduiEngine.initialize(
        config = SduiConfig(
            baseUrl = "https://api.example.com/",          // note the trailing slash
            defaultHeaders = mapOf("Authorization" to "Bearer <token>")
        )
    )
}
```

`SduiConfig` is **memory-only** — it is never serialized, persisted, or logged. See
[09 — Security & configuration](./09-security-and-config.md).

---

## 5. Render a screen

`SduiNavDisplay` is the preconfigured Navigation 3 container. You own the `NavBackStack` and
the navigation/snackbar callbacks:

```kotlin
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import `in`.shanudevcodes.sdui.core.presentation.SduiNavDisplay
import `in`.shanudevcodes.sdui.core.presentation.SduiRoute
import `in`.shanudevcodes.sdui.feature.screen.data.ScreenRepositoryImpl
import kotlinx.coroutines.launch

@Composable
fun App() {
    remember {
        SduiEngine.initialize(SduiConfig(baseUrl = "https://api.example.com/"))
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val repository = remember { ScreenRepositoryImpl() }
    val backStack = remember { NavBackStack<SduiRoute>(SduiRoute.Screen("home")) }

    MaterialTheme {
        Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { _ ->
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

The engine fetches `https://api.example.com/sdui/screens/home`, validates it, and renders it.
See [08 — Navigation](./08-navigation.md) for how `onNavigate` drives the back stack.

---

## 6. Serve a screen

Your backend must expose:

```
GET {baseUrl}sdui/screens/{screenId}
```

returning a screen JSON document (see [02 — JSON schema reference](./02-json-schema-reference.md)).

A minimal `home` screen:

```json
{
  "screenId": "home",
  "schemaVersion": "1.0.0",
  "title": "Home",
  "root": {
    "type": "Column",
    "modifiers": [ { "type": "fillMaxSize" }, { "type": "padding", "value": 24 } ],
    "props": { "verticalArrangement": "Center", "horizontalAlignment": "CenterHorizontally" },
    "children": [
      { "type": "Text", "props": { "text": "Hello from the server!", "style": { "fontSize": 22, "fontWeight": "Bold" } } },
      { "type": "Spacer", "modifiers": [ { "type": "height", "value": 16 } ] },
      {
        "type": "Button",
        "props": { "text": "Show a message", "variant": "filled" },
        "modifiers": [ { "type": "clickable", "value": { "type": "ShowSnackbar", "message": "It works!" } } ]
      }
    ]
  }
}
```

---

## 7. Run it locally without a backend

The repository ships sample screens under `server/sdui/screens/`. Serve them with any static server:

```bash
python -m http.server 8085 --directory server
```

Set your `baseUrl` to your machine address (e.g. `http://192.168.1.10:8085/`) and use a start
route that matches a file name (`complex`, `promotions`, or `settings`).

> On Android emulators, `localhost` refers to the emulator, not your machine. Use your LAN IP, and
> for cleartext HTTP during development add a network security config / `usesCleartextTraffic`.

---

## Next steps

- [02 — JSON schema reference](./02-json-schema-reference.md)
- [03 — Components](./03-components.md)
- [05 — Actions & state](./05-actions-and-state.md)
