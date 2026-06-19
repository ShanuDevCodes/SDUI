# Contributing to SDUI

Thank you for contributing. This document is **mandatory reading** before writing code or opening a PR. Every rule here exists to prevent merge conflicts, regressions, and pattern drift.

---

## 1. Before you start

1. **Verify your environment**: Android Studio Ladybug/Meerkat+, JDK 17+, Kotlin 2.4.0+.

---

## 2. Branch & commit rules

- One feature or fix per branch: `feature/`, `fix/`, `refactor/`, `test/`
- One atomic step per commit — small, independently verifiable, reversible
- Commit message format: `type: short description` (e.g. `fix: null crash in ProductRepository`)
- **Never commit to `main` directly** — always open a PR

---

## 3. Architecture rules (non-negotiable)

### Feature structure
Every feature **must** follow this package layout inside the `shared` module:
```
shared/src/commonMain/kotlin/in/shanudevcodes/sdui/feature/<name>/
├── domain/
│   ├── model/          # Pure Kotlin data classes only — no Room/Ktor/Android/JVM imports
│   ├── repository/     # Interfaces only — no implementations
│   └── usecase/        # Business logic (only when real logic exists)
├── data/
│   ├── mapper/         # ONLY place allowed to import both entity/DTO and domain model
│   └── <Name>RepositoryImpl.kt
├── di/
│   └── <Name>Module.kt
└── presentation/
    ├── <Name>ViewModel.kt
    ├── <Name>Screen.kt
    └── components/
```

### File Atomicity (Mandatory across all layers)
To prevent bloated files, avoid merge conflicts, and keep the codebase modular, **every file must be strictly atomic**. This means each file must contain exactly one class, interface, function, or configuration object.

This rule is strictly enforced across all layers of a feature:
- **Domain Layer**:
  - Each domain model (`*Model.kt`), repository interface (`*Repository.kt`), and usecase (`*UseCase.kt`) must reside in its own dedicated file.
- **Data Layer**:
  - The repository implementation (`*RepositoryImpl.kt`), each data source, and each mapping function or class must be defined in its own file.
- **Presentation Layer**:
  - The stateful screen wrapper (`*Screen.kt`), stateless content layout (`*Content.kt`), and ViewModel (`*ViewModel.kt`) must each have their own separate files.
  - UI state models (`*UiState.kt`), event classes (`*Event.kt`), and side-effect events (`*UiEvent.kt`) must not be bundled together or inside the ViewModel file; each must reside in a separate dedicated file.
  - All sub-composables, shimmers, local helper functions, sorting logic, and configuration constants/maps must be extracted into their own individual, atomic `.kt` files inside the `components/` subfolder.

### The golden rule (enforced by code style and reviews)
**No network DTO or database entity import in any `presentation/` file — ever.**

```kotlin
// ❌ NEVER in a ViewModel or Screen
import io.ktor.client.*
import in.shanudevcodes.sdui.data.model.ProductResponseDto

// ✅ Only domain models
import in.shanudevcodes.sdui.feature.product.domain.model.Product
```

### Dependency direction
```
presentation → domain ← data
```
- `presentation` depends on `domain` interfaces — never on `data` concretions  
- `data` depends on `domain` interfaces — implements them  
- `domain` depends on nothing in the project

### ViewModels
- Inject `CoroutineDispatcher` (defaulted to `Dispatchers.Default`) for all `viewModelScope.launch` calls — required for testability
- Expose state via `StateFlow` / `SharedFlow` — no `LiveData`
- No direct framework/toast calls inside stateless `*Content` composables — keep them pure

### Screen structure (mandatory)
Every screen **must** be split into separate, atomic files:

1. **Stateful Screen (`*Screen.kt`)**: Placed in the root of the feature's `presentation` folder. This is a thin stateful wrapper containing **only** DI injection, state collection, and delegation to the stateless content.
2. **Stateless Content (`*Content.kt`)**: Placed in the root of the feature's `presentation` folder. This contains the layout logic and must be pure, previewable, and testable without DI. The `@Preview` function for the content can remain at the bottom of this file.
3. **Components & Helpers (`components/` subfolder)**:
   - Sub-composables, custom layouts, and shimmers must go in their own dedicated files inside the `components/` subfolder.
   - Helper/utility functions, sorting logic, and configuration maps/constants must be extracted into their own individual, atomic `.kt` files inside the `components/` subfolder.
   - Absolutely no bloated files containing multiple unrelated top-level classes, functions, or configurations are permitted.

Rules:
- `*Screen` — injects ViewModel, collects state, delegates to `*Content`
- `*Content` — pure composable, no DI calls, no state collection, previewable without DI
- If `*Content` has `while(true)` animation loops, add `enableAnimations: Boolean = true` param and guard every loop: `LaunchedEffect(Unit) { if (!enableAnimations) return@LaunchedEffect; while(true) { ... } }`

### State and Event Separation
- **Separate Files**: Do not keep `*UiState` or `*Event` classes inside the same file as the ViewModel or Screen. Place each of them in their own dedicated `.kt` file at the root of the screen/feature package to keep files clean and readable.
- **Unidirectional Event Pipeline**:
  - Composables must not invoke ViewModel functions directly. Instead, all user interactions must be sent to the ViewModel as UI Events via `viewModel.onEvent(event)`.
  - Define `*Event.kt` for UI actions sent *to* the ViewModel (e.g. `ProductEvent.LoadProducts`).
  - Define `*UiEvent.kt` for navigation or side-effect events sent *from* the ViewModel back to the screen (e.g. `ProductUiEvent.NavigateToDetails`).

### Dependency Injection
- Every feature owns its own `di/<Name>Module.kt`
- Register dependencies using interface binding
- Register the module in all platform entry points

---

## 4. Testing rules

**Tests are not optional. PRs without tests for new or changed code will not be merged.**

Every PR must include tests covering what was added or changed:

| What you wrote | Tests required |
|---------------|----------------|
| Domain model + mapper | Mapper test (every field, every null case) |
| Repository implementation | Repository test via fake (all methods) |
| Use case with real logic | Use case test (all branches) |
| ViewModel | VM test (initial state, state transitions, all public functions) |
| New screen `*Content` composable | Compose UI test (content state + empty/error state) |
| Bug fix | Regression test that would have caught the bug |

### How to write tests

- **Test location**: `shared/src/commonTest/kotlin/in/shanudevcodes/sdui/`
- **Use fakes** — do not create platform-specific mocks in commonTest that break multiplatform targets
- **Inject `TestDispatcher`** via the `dispatcher` constructor param in ViewModels
- **No real I/O** — use `FakeRepository`, mock HTTP clients, etc.
- **Naming**: `methodOrScenario_condition_expectedResult` e.g. `toDomain_nullProductId_mapsToNull`

### Run before pushing
```bash
./gradlew :shared:jvmTest
```
All existing tests must still pass. New tests must pass.

---

## 5. What NOT to do

| ❌ Don't | ✅ Do instead |
|----------|--------------|
| Add logic to `data/mapper/` | Logic goes in `domain/usecase/` |
| Use concrete data sources or HttpClient directly in a ViewModel | Create a feature repository interface |
| Add to a global shared module config | Create a per-feature DI module |
| Add `viewModelScope.launch {` without dispatcher | `viewModelScope.launch(dispatcher) {` |
| Put feature-specific UI in core/common | Put it in `feature/<name>/presentation/components/` |
| Put DI or nav scope in `*Content` composable | Keep those in the stateful `*Screen` wrapper |
| Leave `while(true)` loops unguarded in `*Content` | Add `enableAnimations: Boolean = true` and guard the loop |
| Keep helpers, configurations, or multiple composables/functions in Screen/Content files | Extract each helper function, constant, configuration map, and sub-composable into its own atomic `.kt` file under `components/` |
| Wildcard imports (`import in.shanudevcodes.sdui.*`) | Explicit imports only |
| Commit generated files (`build/`, `.idea/`) | They're gitignored — don't force-add |

---

## 6. PR checklist

Before requesting review, verify every item:

- [ ] `./gradlew compileKotlinMetadata` is green
- [ ] `./gradlew :shared:jvmTest` is green
- [ ] **New/changed code has tests** — mapper, repository, use case, ViewModel, and Compose UI as applicable
- [ ] No network DTO or database entity imports in any `presentation/` file
- [ ] New feature has: domain model, repository interface, mapper, DI module, mapper test
- [ ] Screen has stateless `*Content(params, lambdas)` composable + `@Preview`
- [ ] **Files are atomic**: No helper functions, configuration constants, or sub-composables are in screen or content files; each resides in its own `.kt` file under `components/`
- [ ] ViewModel has CoroutineDispatcher constructor param with default
- [ ] Feature DI module registered in platform entry points
- [ ] PR description explains what changed and what was tested
