# 10 — Troubleshooting

Common issues when integrating SDUI, and how to diagnose them.

---

## The fallback screen appears instead of my UI

SDUI renders a native `FallbackScreen` (error message + retry button) instead of crashing whenever a screen
cannot be loaded or validated. Causes:

| Cause | Fix |
|-------|-----|
| Network/HTTP error fetching the screen | Check `baseUrl` (trailing slash), connectivity, headers, and that `GET {baseUrl}sdui/screens/{screenId}` returns 2xx. |
| `schemaVersion` higher than `1.0.0` | Lower the version, or upgrade the library when newer schema support ships. |
| `root.type` blank/missing | Ensure the root node has a valid `type`. |
| Tree deeper than 50 levels | Flatten the component tree. |
| Malformed JSON | Validate the payload; the deserializer is lenient but structurally-invalid JSON fails. |

The retry button re-triggers the fetch, so transient network errors recover without restarting.

---

## A component doesn't appear

- **Unknown `type`.** Unregistered component types are skipped with a console warning
  (`WARNING: Unresolved component type: X`). Check spelling, and for custom components ensure
  `registerComponent` ran **before** the screen rendered.
- **Zero size.** A node with no size modifier inside a container that gives it no space may render at 0dp.
  Add `fillMaxWidth`, `width`/`height`, or `weight`.
- **Conditional hid it.** A `Conditional`/`Visible` wrapper may be evaluating false — check the bound state
  key and operator.

---

## Images or icons are blank

- Coil isn't configured. Add `KtorNetworkFetcherFactory()` (network) and `SvgDecoder.Factory()` (SVG) to
  the singleton image loader — see [01 — Getting started](./01-getting-started.md).
- The `Icon` `url` is wrong. Icons are **URLs**, not Material icon names. Point to an SVG/PNG, e.g.
  `https://fonts.gstatic.com/s/i/materialiconsoutlined/search/v1/24px.svg`.
- Cleartext HTTP blocked. On Android, `http://` image URLs require a network security config in dev.

---

## Icon is huge or fills the screen

Without a size modifier, `Icon` defaults to 24dp. If you wrapped it in a fill modifier or a parent forcing
size, constrain it: `{ "type": "size", "value": 24 }`.

---

## State / templates don't update

- **Wrong `stateKey`.** The input's `stateKey` must match the template `{{key}}` and any `UpdateState`
  `stateKey`.
- **Not seeded.** Add the key to `initialState` (or pass it via navigation `params`) so it exists before
  first render.
- **Template not in a string prop.** `{{...}}` resolution applies to string props (and `ApiCall.body`),
  not numbers or booleans.

---

## Actions don't fire

| Symptom | Likely cause |
|---------|--------------|
| `Navigate`/`GoBack` do nothing | `onNavigate` not wired, or back guard popping the root. Verify your `SduiNavDisplay` callback. |
| `Custom`/`Track` do nothing | These need an `actionInterceptor`; the default flow has no dedicated callback. Add one in `SduiConfig`. |
| `PopToRoot`/`DeepLink` do nothing | Handle via `actionInterceptor` or custom back-stack logic. |
| `ApiCall` never calls back | Check the engine is initialized, `endpoint` is correct (relative to `baseUrl`), and inspect `onSuccess`/`onError`. |
| `clickable` not triggering | `value` must be a valid action object; malformed actions are ignored. |

See [05 — Actions & state](./05-actions-and-state.md).

---

## `IllegalStateException: SduiEngine not initialized`

`SduiEngine.initialize(config)` must run before any fetch/render or `getConfig()`/`getHttpClient()` call.
Wrap the init in `remember {}` at the top of your composition, or call it in your app startup.

---

## Build error: namespace used in multiple modules

If you embed the library sources directly (rather than the published artifact) alongside an app with the
same Android namespace, give each module a unique `namespace`. The published `:sdui` artifact uses
`in.shanudevcodes.sdui.lib` to avoid clashing with apps using `in.shanudevcodes.sdui`.

---

## GitHub Packages: 401 / cannot resolve dependency

- Provide `gpr.user` + `gpr.token` (PAT with `read:packages`) in `~/.gradle/gradle.properties`, or set
  `GITHUB_ACTOR` / `GITHUB_TOKEN` env vars.
- Confirm the repository URL: `https://maven.pkg.github.com/shanudeveloper/sdui`.

---

## Tests: which Gradle task?

- **Engine/library tests:** `./gradlew :sdui:jvmTest` (this is the meaningful suite).
- **iOS library tests (macOS):** `./gradlew :sdui:iosSimulatorArm64Test`.
- `:shared:jvmTest` only runs the demo module's trivial stub tests.

---

## Still stuck?

- Check the console for `WARNING:`/`ERROR:` logs from the renderer (unknown component, depth exceeded).
- Reproduce against the bundled sample screens (`server/sdui/screens/`) served locally to isolate whether
  the issue is your payload or your integration.
- Verify your payload against [02 — JSON schema reference](./02-json-schema-reference.md).
