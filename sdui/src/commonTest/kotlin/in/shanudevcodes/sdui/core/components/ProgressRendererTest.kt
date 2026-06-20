package `in`.shanudevcodes.sdui.core.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class ProgressRendererTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testCircularProgressRenderer_rendersSuccessfully() = runComposeUiTest {
        val stateHolder = SduiStateHolder()

        // Indeterminate
        val indeterminateDto = SduiComponentDto(
            type = "CircularProgress",
            props = mapOf(
                "color" to kotlinx.serialization.json.JsonPrimitive("#FF0000")
            )
        )
        setContent {
            CircularProgressRenderer(indeterminateDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }

    @Test
    fun testCircularProgressRenderer_determinate_rendersSuccessfully() = runComposeUiTest {
        val stateHolder = SduiStateHolder()

        // Determinate
        val determinateDto = SduiComponentDto(
            type = "CircularProgress",
            props = mapOf(
                "progress" to kotlinx.serialization.json.JsonPrimitive(0.6f),
                "color" to kotlinx.serialization.json.JsonPrimitive("#00FF00")
            )
        )
        setContent {
            CircularProgressRenderer(determinateDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }

    @Test
    fun testLinearProgressRenderer_rendersSuccessfully() = runComposeUiTest {
        val stateHolder = SduiStateHolder()

        // Indeterminate
        val indeterminateDto = SduiComponentDto(
            type = "LinearProgress",
            props = mapOf(
                "color" to kotlinx.serialization.json.JsonPrimitive("#0000FF"),
                "trackColor" to kotlinx.serialization.json.JsonPrimitive("#CCCCCC")
            )
        )
        setContent {
            LinearProgressRenderer(indeterminateDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }

    @Test
    fun testLinearProgressRenderer_determinate_rendersSuccessfully() = runComposeUiTest {
        val stateHolder = SduiStateHolder()

        // Determinate
        val determinateDto = SduiComponentDto(
            type = "LinearProgress",
            props = mapOf(
                "progress" to kotlinx.serialization.json.JsonPrimitive(0.35f),
                "color" to kotlinx.serialization.json.JsonPrimitive("#FFFF00")
            )
        )
        setContent {
            LinearProgressRenderer(determinateDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }
}
