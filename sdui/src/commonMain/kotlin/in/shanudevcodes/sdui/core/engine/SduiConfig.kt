package `in`.shanudevcodes.sdui.core.engine

import io.ktor.client.request.HttpRequestBuilder
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto

/**
 * Configuration for the SDUI engine, injected by the host application.
 * Kept memory-only and never serialized to protect sensitive keys and urls.
 */
public data class SduiConfig(
    val baseUrl: String,
    val defaultHeaders: Map<String, String> = emptyMap(),
    val requestInterceptor: ((HttpRequestBuilder) -> Unit)? = null,
    val actionInterceptor: ((SduiActionDto) -> Boolean)? = null
) {
    override fun toString(): String {
        return "SduiConfig(baseUrl=***, defaultHeaders=***, requestInterceptor=***, actionInterceptor=***)"
    }
}
