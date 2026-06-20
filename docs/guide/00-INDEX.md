# SDUI Developer Guide

Extensive documentation for developers integrating **SDUI** into their own Compose Multiplatform app.

If you are new, read in order. If you are looking something up, jump to the relevant page.

| # | Page | What it covers |
|---|------|----------------|
| 01 | [Getting started](./01-getting-started.md) | Install, Coil setup, your first screen, running locally |
| 02 | [JSON schema reference](./02-json-schema-reference.md) | Every field of the screen and node contract |
| 03 | [Components](./03-components.md) | Every built-in component with prop tables + examples |
| 04 | [Modifiers](./04-modifiers.md) | Every modifier with value shapes + examples |
| 05 | [Actions & state](./05-actions-and-state.md) | Action pipeline, interceptor, state holder, templates, API calls |
| 06 | [Theming](./06-theming.md) | All Material 3 color roles, shapes, typography overrides |
| 07 | [Custom components](./07-custom-components.md) | Register native composables, prop helpers, overriding built-ins |
| 08 | [Navigation](./08-navigation.md) | `SduiRoute`, `NavBackStack`, params, back handling |
| 09 | [Security & configuration](./09-security-and-config.md) | `SduiConfig`, memory-only model, headers, interceptors |
| 10 | [Troubleshooting](./10-troubleshooting.md) | Validation failures, fallback screen, common errors |

---

## At a glance

- **Package root:** `in.shanudevcodes.sdui`
- **Artifact:** `in.shanudevcodes:sdui:0.1.0` (GitHub Packages)
- **Entry points:** `SduiEngine`, `SduiConfig`, `SduiNavDisplay`, `SduiRoute`, `ScreenRepositoryImpl`
- **Screen endpoint:** `GET {baseUrl}sdui/screens/{screenId}`
- **Schema version supported:** `1.0.0` (same-or-lower accepted; higher rejected to fallback)

For a one-page overview, see the repository [README](../../README.md).
