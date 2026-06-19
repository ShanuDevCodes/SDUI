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
object SduiEngine {
    private var config: SduiConfig? = null
    private var client: HttpClient? = null

    /**
     * Initializes the engine with the host app configuration.
     * Builds the underlying HttpClient configured with JSON parsing and interceptors.
     * An optional [HttpClientEngine] can be provided (e.g. for testing with MockEngine).
     */
    fun initialize(config: SduiConfig, httpClientEngine: io.ktor.client.engine.HttpClientEngine? = null) {
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
                    prettyPrint = true
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

    /**
     * Returns the active configuration, throwing if not initialized.
     */
    fun getConfig(): SduiConfig {
        return config ?: throw IllegalStateException("SduiEngine has not been initialized. Call SduiEngine.initialize(config) first.")
    }

    /**
     * Returns the configured HttpClient, throwing if not initialized.
     */
    fun getHttpClient(): HttpClient {
        return client ?: throw IllegalStateException("SduiEngine has not been initialized. Call SduiEngine.initialize(config) first.")
    }

    /**
     * Registers a custom component renderer. If a component with the same [type] name
     * is already registered (such as a built-in layout or display renderer), this will
     * override it.
     *
     * @param type The type identifier of the custom component (e.g. "SponsorCard").
     * @param renderer The Compose Composable rendering block for the component.
     */
    fun registerComponent(type: String, renderer: ComponentRenderer) {
        ComponentRegistry.register(type, renderer)
    }

    /**
     * Helper to clear engine state in tests.
     */
    fun reset() {
        config = null
        client = null
    }
}
