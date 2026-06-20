import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    `maven-publish`
}

kotlin {
    explicitApi()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Sdui"
            isStatic = true
        }
    }

    jvm()

    androidLibrary {
        namespace = "in.shanudevcodes.sdui.lib"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Navigation 3
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.androidx.navigation3.runtime)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Coil (needed by ImageRenderer, IconRenderer)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.coil.svg)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.ktor.client.mock)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        jvmTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}


// ---------------------------------------------------------------------------
// Maven publishing — target: GitHub Packages
// Publish: ./gradlew :sdui:publishAllPublicationsToGitHubPackagesRepository
//   Requires env vars: GITHUB_ACTOR, GITHUB_TOKEN
// ---------------------------------------------------------------------------
group = "in.shanudevcodes"
version = "0.1.0"

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/shanudeveloper/sdui")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: ""
                password = System.getenv("GITHUB_TOKEN") ?: ""
            }
        }
    }
    publications.withType<MavenPublication> {
        pom {
            name.set("SDUI")
            description.set("Kotlin Multiplatform Server-Driven UI engine for Compose Multiplatform")
            url.set("https://github.com/shanudeveloper/sdui")
            licenses {
                license {
                    name.set("Apache-2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0")
                }
            }
            developers {
                developer {
                    id.set("shanudeveloper")
                    name.set("Shanu Dev")
                }
            }
            scm {
                connection.set("scm:git:github.com/shanudeveloper/sdui.git")
                url.set("https://github.com/shanudeveloper/sdui")
            }
        }
    }
}
