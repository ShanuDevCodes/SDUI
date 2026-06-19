# Project-Scoped Rules & Guardrails — SDUI Library

These guidelines are **strictly mandatory** for all development and refactoring tasks in this project. All agents must read and adhere to them without exception.

---

## 1. Incremental, Atomic Development Workflow
- **One Task at a Time**: Work must be broken down into atomic steps. Do not attempt to code multiple independent tickets or features concurrently.
- **Immediate Progress Tracking**: Keep [CONTEXT_TRACKER.md](file:///C:/Users/KIIT0001/androidstudioprojects/sdui/docs/work/CONTEXT_TRACKER.md) updated immediately after completing each step. Record completed items, updated statuses, and outstanding tickets.
- **Verification**: Run tests (`./gradlew :shared:jvmTest`) or verify metadata compilation (`./gradlew compileKotlinMetadata`) after each atomic task before marking it as complete.

---

## 2. File Atomicity & Code Structure (Non-Negotiable)
- **Dedicated Files Only**: Every class, interface, view model, state class, event type, sub-composable, helper utility, and configuration constant MUST reside in its own dedicated, atomic Kotlin file.
- **Strictly No Bundling**: Do not group separate classes (e.g. putting `SduiScreenUiState`, `SduiScreenEvent`, and `SduiScreenUiEvent` in the same file as `SduiScreenViewModel.kt` or `SduiScreen.kt`). They must be separate files.
- **Sub-Composables & Constants**: All layout components, shimmers, static styling mappings, and utility functions must be extracted into their own individual files inside the feature's `components/` subfolder.

---

## 3. Strict Clean Architecture & MVVM Guidelines
- **Dependency Flow**: The package dependency hierarchy is strictly unidirectional:
  ```
  presentation ──▶ domain ◀── data
  ```
- **Layer Boundaries**:
  - **Presentation Layer (`presentation/`)**: Compose UI screens, ViewModels, and state. **Never** import network library files (e.g., Ktor `HttpClient`), Kotlinx Serialization annotations, or network Data Transfer Objects (DTOs) here.
  - **Domain Layer (`domain/`)**: Pure Kotlin business models (e.g. `SduiNode`), repository interfaces, and usecases. Has zero dependencies on any external framework or the data layer.
  - **Data Layer (`data/`)**: Repository implementations, client database helpers, and network fetching logic. This is the **only** layer allowed to map raw DTOs into domain models via mapper files.

---

## 4. PayPay-Inspired Reliability & Compatibility Specs
- **Sealed Node Tree**: The UI must be represented in the domain layer as a compile-time type-safe sealed hierarchy (`SduiNode`) instead of passing untyped generic props maps through components.
- **Schema Validation**: Parse and validate `schemaVersion` before rendering. Reject payloads exceeding the client's max supported version.
- **Strict Pre-Render Verification**: Check for required properties and valid types during mapping. If a critical node is malformed, abort rendering immediately.
- **Safe Fallback**: Never let parsing or rendering throw unhandled exceptions that crash the host app. If validation fails or a version mismatch occurs, display a native `FallbackScreen` or redirect according to configuration.
- **Offline Reliability**: Implement offline caching of screen configurations and fallback to local JSON templates packaged in the app assets.
- **Feature Flags**: Evaluate custom feature flags in the rendering tree for conditional UI layouts.

---

## 5. Security Guardrails
- **Memory-Only Config**: All host-application injected configuration (contained in `SduiConfig` like base URLs, tokens, and headers) must be kept strictly in memory.
- **No Leakage**: Never serialize `SduiConfig`, write it to disk, store it in backend databases, log it in production, or send it in request bodies.
- **Enforced Headers**: Pass authentication parameters as HTTP headers, never inside JSON bodies or query parameters.
