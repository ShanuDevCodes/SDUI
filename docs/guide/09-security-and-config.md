# 09 — Security & configuration

SDUI follows a **memory-only configuration** model: everything the host injects (base URL, headers,
tokens) lives only in memory and is never serialized, persisted, or logged.

---

## SduiConfig

```kotlin
data class SduiConfig(
    val baseUrl: String,
    val defaultHeaders: Map<String, String> = emptyMap(),
    val requestInterceptor: ((HttpRequestBuilder) -> Unit)? = null,
    val actionInterceptor: ((SduiActionDto) -> Boolean)? = null
)
```

| Field | Purpose |
|-------|---------|
| `baseUrl` | Base URL for screen fetches and relative `ApiCall` endpoints. Include the trailing slash. |
| `defaultHeaders` | Headers added to every request (e.g. `Authorization`). |
| `requestInterceptor` | Lambda to mutate each outgoing request (dynamic tokens, tracing headers). |
| `actionInterceptor` | Lambda invoked for every action; return `true` to consume it. |

`SduiConfig.toString()` is **redacted** — it prints `baseUrl=***, defaultHeaders=***, …` so config never
leaks into logs or crash reports.

---

## Initializing

```kotlin
SduiEngine.initialize(
    SduiConfig(
        baseUrl = "https://api.example.com/",
        defaultHeaders = mapOf(
            "Authorization" to "Bearer $token",
            "X-App-Version" to appVersion
        )
    )
)
```

For tests, you can pass a Ktor `MockEngine`:

```kotlin
SduiEngine.initialize(config = SduiConfig(baseUrl = "https://test/"), httpClientEngine = mockEngine)
```

`SduiEngine` is a singleton; call `SduiEngine.reset()` between tests.

---

## Authentication

Pass credentials as **HTTP headers**, never in JSON bodies or query params.

### Static token

```kotlin
SduiConfig(
    baseUrl = "https://api.example.com/",
    defaultHeaders = mapOf("Authorization" to "Bearer $token")
)
```

### Dynamic / refreshing token

Use `requestInterceptor` to inject the current token at request time:

```kotlin
SduiConfig(
    baseUrl = "https://api.example.com/",
    requestInterceptor = { request ->
        request.headers.append("Authorization", "Bearer ${tokenStore.current()}")
    }
)
```

`requestInterceptor` runs on every request the engine makes (screen fetches and `ApiCall`s).

---

## What the engine guarantees

| Guarantee | Detail |
|-----------|--------|
| Memory-only config | `SduiConfig` is not `@Serializable`; never written to disk or DB. |
| Redacted logging | `toString()` masks all fields. |
| Header-based auth | Tokens travel as headers, not in payloads. |
| Recursion cap | Component tree depth is capped at **50** in both validator and renderer (DoS mitigation). |
| Safe fallback | Invalid/incompatible payloads render `FallbackScreen` instead of crashing. |
| Unknown nodes skipped | Unrecognized component types are skipped with a warning; siblings still render. |

---

## Your responsibilities

These are **not** yet enforced by the engine (on the roadmap) — handle them on your side:

- **HTTPS in production.** The engine does not currently reject `http://` base URLs. Use HTTPS for any
  non-local environment. For local dev over cleartext, configure the platform (Android network security
  config / `usesCleartextTraffic`).
- **Response size.** There is no built-in payload cap yet; ensure your backend returns bounded screen
  documents.
- **Per-field validation.** The validator checks schema version, root type, and depth — not every
  per-component required field. Treat server JSON as your contract and validate server-side.
- **Absolute `ApiCall` URLs.** Prefer relative endpoints under your `baseUrl`. Absolute-URL rejection is
  not yet enforced.
- **Token storage.** SDUI keeps the config in memory; how you obtain/refresh/store the token before passing
  it in is your concern (use the platform secure storage).

---

## Trust model

Treat the server payload as **semi-trusted input**:

- The engine will not crash on malformed JSON, but it will render whatever valid UI the server sends.
- `clickable`/action payloads come from the server — your `actionInterceptor` is the gate for
  sensitive/native actions (checkout, deep links, payments). Validate `Custom` action names and payloads
  before acting.
- Never let server JSON drive privileged native operations without host-side checks.

```kotlin
actionInterceptor = { action ->
    if (action.type == "Custom" && action.name == "startPayment") {
        if (isPaymentContextValid()) startPayment(action.payload)
        true  // consumed regardless; do not fall through to engine default
    } else false
}
```

---

## Multiple configurations

`SduiEngine` is a global singleton holding one config/client. If your app embeds SDUI in flows that need
different base URLs or auth, you currently re-`initialize()` when switching contexts. A per-instance engine
is on the roadmap.
