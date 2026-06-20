This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM). It is structured as a
**publishable SDUI library** plus a set of sample apps that demonstrate it.

### Modules

* **`/sdui`** — the publishable SDUI engine library (Server-Driven UI for Compose Multiplatform).
  Contains the `core/`, `feature/`, and `navigation/` packages. This is the artifact consumers depend on.
  It carries no hardcoded URLs and registers no demo components.

* **`/shared`** — demo/sample shared Compose UI (`App.kt`, `SduiBannerCard.kt`) used by all three sample
  apps. Depends on and re-exports `:sdui` so the iOS `Shared.framework` keeps working.

* [/iosApp](./iosApp/iosApp) contains the iOS sample application entry point (SwiftUI host).

* `/androidApp`, `/desktopApp` — Android and Desktop (JVM) sample app entry points.

> Note: the engine sources historically lived under `/shared`; they are being moved to the dedicated
> `/sdui` library module as part of the library-readiness work (see `docs/work/09-LIBRARY_AUDIT.md`).

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks (library tests live in `:sdui`):

- Android tests: `./gradlew :sdui:testAndroidHostTest`
- Desktop tests: `./gradlew :sdui:jvmTest`
- iOS tests: `./gradlew :sdui:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…