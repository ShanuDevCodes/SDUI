package `in`.shanudevcodes.sdui.core.engine

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import `in`.shanudevcodes.sdui.core.network.createHttpClient
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.core.registry.ComponentRenderer

/**
 * Singleton engine that manages the configuration, interceptors, and configured Ktor HttpClient
 * for Server-Driven UI fetching and actions.
 */
public object SduiEngine {
    private var config: SduiConfig? = null
    private var client: HttpClient? = null

    /**
     * Initializes the engine with the host app configuration.
     * An optional [HttpClientEngine] can be provided (e.g. for testing with MockEngine).
     */
    public fun initialize(config: SduiConfig, httpClientEngine: io.ktor.client.engine.HttpClientEngine? = null) {
        this.config = config
        val baseClient = if (httpClientEngine != null) {
            HttpClient(httpClientEngine)
        } else {
            createHttpClient()
        }
        this.client = baseClient.config {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            defaultRequest {
                url(config.baseUrl)
                config.defaultHeaders.forEach { (key, value) ->
                    header(key, value)
                }
            }
            val interceptorPlugin = io.ktor.client.plugins.api.createClientPlugin("SduiRequestInterceptor") {
                onRequest { request, _ ->
                    config.requestInterceptor?.invoke(request)
                }
            }
            install(interceptorPlugin)
        }
    }

    /** Returns the active configuration, throwing if not initialized. */
    public fun getConfig(): SduiConfig =
        config ?: throw IllegalStateException("SduiEngine not initialized. Call SduiEngine.initialize() first.")

    /** Returns the configured HttpClient, throwing if not initialized. */
    public fun getHttpClient(): HttpClient =
        client ?: throw IllegalStateException("SduiEngine not initialized. Call SduiEngine.initialize() first.")

    /**
     * Registers a custom component renderer by [type] identifier.
     * Overrides any existing registration including built-ins.
     */
    public fun registerComponent(type: String, renderer: ComponentRenderer) {
        ComponentRegistry.register(type, renderer)
    }

    /** Resets engine state. Intended for use in tests only. */
    public fun reset() {
        config = null
        client = null
    }
}
