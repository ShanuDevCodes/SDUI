# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class **$$serializer { *; }
-keep,includedescriptorclasses class in.shanudevcodes.sdui.**$$serializer { *; }
-keepclassmembers @kotlinx.serialization.Serializable class in.shanudevcodes.sdui.** {
    *** Companion;
    *** INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn io.ktor.**

# Coil
-keep class coil3.** { *; }
-dontwarn coil3.**

# OkHttp (used by Ktor on Android)
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep SduiEngine and public API surface
-keep class in.shanudevcodes.sdui.core.engine.** { *; }
-keep class in.shanudevcodes.sdui.core.schema.** { *; }
