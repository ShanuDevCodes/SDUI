package `in`.shanudevcodes.sdui.core.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runComposeUiTest
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.SduiStyleDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class DisplayComponentsTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testTextRenderer_rendersSuccessfully() = runComposeUiTest {
        val styleJson = kotlinx.serialization.json.Json.encodeToJsonElement(
            SduiStyleDto.serializer(),
            SduiStyleDto(
                fontSize = 18,
                fontWeight = "Bold",
                fontStyle = "Italic",
                color = "#FF0000",
                textAlign = "Center",
                maxLines = 2,
                overflow = "Ellipsis"
            )
        )
        val textDto = SduiComponentDto(
            type = "Text",
            props = mapOf(
                "text" to kotlinx.serialization.json.JsonPrimitive("Hello SDUI"),
                "style" to styleJson
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            TextRenderer(textDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }

    @Test
    fun testIconRenderer_rendersSuccessfully() = runComposeUiTest {
        val iconDto = SduiComponentDto(
            type = "Icon",
            props = mapOf(
                "name" to kotlinx.serialization.json.JsonPrimitive("home"),
                "tint" to kotlinx.serialization.json.JsonPrimitive("#00FF00")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            IconRenderer(iconDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }

    @Test
    fun testDividerRenderer_rendersSuccessfully() = runComposeUiTest {
        val dividerDto = SduiComponentDto(
            type = "Divider",
            props = mapOf(
                "thickness" to kotlinx.serialization.json.JsonPrimitive(2),
                "color" to kotlinx.serialization.json.JsonPrimitive("#0000FF")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            DividerRenderer(dividerDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }

    @Test
    fun testCardRenderer_rendersChildren() = runComposeUiTest {
        var childRenderCount = 0
        ComponentRegistry.register("ChildType") { _, _, _ ->
            childRenderCount++
        }

        val cardDto = SduiComponentDto(
            type = "Card",
            props = mapOf(
                "elevation" to kotlinx.serialization.json.JsonPrimitive(4),
                "shape" to kotlinx.serialization.json.JsonPrimitive("Rounded")
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            CardRenderer(cardDto, Modifier, stateHolder)
        }

        assertEquals(1, childRenderCount, "Card should render all its children")
    }

    @Test
    fun testSurfaceRenderer_rendersChildren() = runComposeUiTest {
        var childRenderCount = 0
        ComponentRegistry.register("ChildType") { _, _, _ ->
            childRenderCount++
        }

        val surfaceDto = SduiComponentDto(
            type = "Surface",
            props = mapOf(
                "color" to kotlinx.serialization.json.JsonPrimitive("#FAFAFA"),
                "elevation" to kotlinx.serialization.json.JsonPrimitive(2),
                "shape" to kotlinx.serialization.json.JsonPrimitive("Circle")
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            SurfaceRenderer(surfaceDto, Modifier, stateHolder)
        }

        assertEquals(2, childRenderCount, "Surface should render all its children")
    }

    @Test
    fun testImageRenderer_rendersSuccessfully() = runComposeUiTest {
        val imageDto = SduiComponentDto(
            type = "Image",
            props = mapOf(
                "url" to kotlinx.serialization.json.JsonPrimitive("https://example.com/logo.png"),
                "contentDescription" to kotlinx.serialization.json.JsonPrimitive("logo"),
                "contentScale" to kotlinx.serialization.json.JsonPrimitive("Crop")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            ImageRenderer(imageDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }
}
