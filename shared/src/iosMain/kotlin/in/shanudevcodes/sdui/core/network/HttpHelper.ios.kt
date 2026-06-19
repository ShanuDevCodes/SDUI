package `in`.shanudevcodes.sdui.core.network

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual fun createHttpClient(): HttpClient {
    return HttpClient(Darwin) {
        engine {
            configureSession {
                timeoutIntervalForRequest = 30.0
                timeoutIntervalForResource = 30.0
            }
        }
    }
}
