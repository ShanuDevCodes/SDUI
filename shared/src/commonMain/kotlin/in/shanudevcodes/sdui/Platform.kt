package `in`.shanudevcodes.sdui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform