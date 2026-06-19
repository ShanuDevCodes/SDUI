package `in`.shanudevcodes.sdui.core.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
