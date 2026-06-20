package `in`.shanudevcodes.sdui.core.platform

internal class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

internal actual fun getPlatform(): Platform = JVMPlatform()
