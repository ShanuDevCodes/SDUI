package `in`.shanudevcodes.sdui.core.engine

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import `in`.shanudevcodes.sdui.core.network.createHttpClient

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
     */
    fun initialize(config: SduiConfig) {
        this.config = config
        this.client = createHttpClient().config {
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
     * Helper to clear engine state in tests.
     */
    fun reset() {
        config = null
        client = null
    }
}
