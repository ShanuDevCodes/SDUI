import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()
    
    androidLibrary {
       namespace = "in.shanudevcodes.sdui.shared"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)

            //ktor-client-okhttp
            implementation("io.ktor:ktor-client-okhttp:3.2.3")
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            //ktor
            implementation("io.ktor:ktor-client-core:3.2.3")
            implementation("io.ktor:ktor-client-content-negotiation:3.2.3")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.3")

            //kotlinx-serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        iosMain.dependencies {
            //ktor-client-darwin
            implementation("io.ktor:ktor-client-darwin:3.2.3")
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}