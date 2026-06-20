package `in`.shanudevcodes.sdui.core.platform

internal interface Platform {
    val name: String
}

internal expect fun getPlatform(): Platform
