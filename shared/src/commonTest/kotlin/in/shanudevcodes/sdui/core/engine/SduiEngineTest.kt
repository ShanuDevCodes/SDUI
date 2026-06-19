package `in`.shanudevcodes.sdui.core.engine

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class SduiEngineTest {

    @BeforeTest
    fun setUp() {
        SduiEngine.reset()
    }

    @AfterTest
    fun tearDown() {
        SduiEngine.reset()
    }

    @Test
    fun testGetConfig_beforeInit_throwsException() {
        assertFailsWith<IllegalStateException> {
            SduiEngine.getConfig()
        }
    }

    @Test
    fun testGetHttpClient_beforeInit_throwsException() {
        assertFailsWith<IllegalStateException> {
            SduiEngine.getHttpClient()
        }
    }

    @Test
    fun testInitialize_storesConfigAndCreatesClient() {
        val config = SduiConfig(
            baseUrl = "https://api.example.com",
            defaultHeaders = mapOf("X-Test" to "Value")
        )

        SduiEngine.initialize(config)

        val retrievedConfig = SduiEngine.getConfig()
        assertEquals("https://api.example.com", retrievedConfig.baseUrl)
        assertEquals("Value", retrievedConfig.defaultHeaders["X-Test"])

        val httpClient = SduiEngine.getHttpClient()
        assertNotNull(httpClient, "HttpClient should be configured")
    }
}
