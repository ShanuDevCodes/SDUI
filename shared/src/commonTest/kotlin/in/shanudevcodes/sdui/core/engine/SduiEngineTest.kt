package `in`.shanudevcodes.sdui.core.engine

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest

@OptIn(ExperimentalTestApi::class)
class SduiEngineTest {

    @BeforeTest
    fun setUp() {
        SduiEngine.reset()
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        SduiEngine.reset()
        ComponentRegistry.reset()
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

    @Test
    fun testRegisterComponent_addsCustomComponent() = runComposeUiTest {
        val type = "MyCustomCard"
        var rendered = false

        SduiEngine.registerComponent(type) { _, _, _ ->
            rendered = true
        }

        setContent {
            val resolved = ComponentRegistry.resolve(type)
            if (resolved != null) {
                resolved(
                    SduiComponentDto(type = type),
                    androidx.compose.ui.Modifier,
                    SduiStateHolder()
                )
            }
        }

        assertTrue(rendered)
    }

    @Test
    fun testRegisterComponent_overridesBuiltIn() = runComposeUiTest {
        val originalTextRenderer = ComponentRegistry.resolve("Text")
        assertNotNull(originalTextRenderer)

        var customRendered = false
        SduiEngine.registerComponent("Text") { _, _, _ ->
            customRendered = true
        }

        setContent {
            val newTextRenderer = ComponentRegistry.resolve("Text")
            if (newTextRenderer != null) {
                newTextRenderer(
                    SduiComponentDto(type = "Text"),
                    androidx.compose.ui.Modifier,
                    SduiStateHolder()
                )
            }
        }

        assertTrue(customRendered)
    }
}
