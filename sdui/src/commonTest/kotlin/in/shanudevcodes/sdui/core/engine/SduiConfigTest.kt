package `in`.shanudevcodes.sdui.core.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SduiConfigTest {

    @Test
    fun testSduiConfig_holdsValuesCorrectly() {
        val config = SduiConfig(
            baseUrl = "https://api.example.com",
            defaultHeaders = mapOf("Authorization" to "Bearer test-token"),
            requestInterceptor = { _ -> },
            actionInterceptor = { _ -> true }
        )

        assertEquals("https://api.example.com", config.baseUrl)
        assertEquals("Bearer test-token", config.defaultHeaders["Authorization"])
        assertNotNull(config.requestInterceptor)
        assertNotNull(config.actionInterceptor)
    }

    @Test
    fun testSduiConfig_toString_redactsSecrets() {
        val config = SduiConfig(
            baseUrl = "https://api.example.com",
            defaultHeaders = mapOf("Authorization" to "Bearer test-token")
        )

        val str = config.toString()
        assertTrue(str.contains("***"), "toString must redact fields")
        assertTrue(!str.contains("api.example.com"), "toString must not leak baseUrl")
        assertTrue(!str.contains("test-token"), "toString must not leak headers")
    }
}
