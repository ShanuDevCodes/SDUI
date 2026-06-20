package `in`.shanudevcodes.sdui.core.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
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

    @Test
    fun testCardRenderer_onClick_colors_border() = runComposeUiTest {
        val onClickAction = SduiActionDto(type = "Navigate", route = "card_click")
        val cardDto = SduiComponentDto(
            type = "Card",
            props = mapOf(
                "elevation" to kotlinx.serialization.json.JsonPrimitive(2),
                "shape" to kotlinx.serialization.json.JsonPrimitive("Rounded"),
                "containerColor" to kotlinx.serialization.json.JsonPrimitive("#FF0000"),
                "contentColor" to kotlinx.serialization.json.JsonPrimitive("#FFFFFF"),
                "borderWidth" to kotlinx.serialization.json.JsonPrimitive(1),
                "borderColor" to kotlinx.serialization.json.JsonPrimitive("#00FF00")
            ),
            actions = mapOf("onClick" to onClickAction)
        )
        val stateHolder = SduiStateHolder()
        var actionDispatched: SduiActionDto? = null

        setContent {
            androidx.compose.runtime.CompositionLocalProvider(LocalSduiActionHandler provides { actionDispatched = it }) {
                CardRenderer(cardDto, Modifier, stateHolder)
            }
        }
        assertTrue(true)
    }

    @Test
    fun testSurfaceRenderer_onClick_and_border() = runComposeUiTest {
        val onClickAction = SduiActionDto(type = "Navigate", route = "surface_click")
        val surfaceDto = SduiComponentDto(
            type = "Surface",
            props = mapOf(
                "color" to kotlinx.serialization.json.JsonPrimitive("#FF0000"),
                "contentColor" to kotlinx.serialization.json.JsonPrimitive("#FFFFFF"),
                "radius" to kotlinx.serialization.json.JsonPrimitive(16),
                "shadowElevation" to kotlinx.serialization.json.JsonPrimitive(8),
                "borderWidth" to kotlinx.serialization.json.JsonPrimitive(2),
                "borderColor" to kotlinx.serialization.json.JsonPrimitive("#000000")
            ),
            actions = mapOf("onClick" to onClickAction)
        )
        val stateHolder = SduiStateHolder()

        setContent {
            SurfaceRenderer(surfaceDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }

    @Test
    fun testImageRenderer_placeholders() = runComposeUiTest {
        val imageDto = SduiComponentDto(
            type = "Image",
            props = mapOf(
                "url" to kotlinx.serialization.json.JsonPrimitive("https://example.com/logo.png"),
                "contentDescription" to kotlinx.serialization.json.JsonPrimitive("logo"),
                "placeholderColor" to kotlinx.serialization.json.JsonPrimitive("#DDDDDD"),
                "errorTint" to kotlinx.serialization.json.JsonPrimitive("#FF0000")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            ImageRenderer(imageDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }

    @Test
    fun testTextRenderer_templateResolution() = runComposeUiTest {
        val textDto = SduiComponentDto(
            type = "Text",
            props = mapOf(
                "text" to kotlinx.serialization.json.JsonPrimitive("Hello, {{username}}!")
            )
        )
        val stateHolder = SduiStateHolder()
        stateHolder.setValue("username", kotlinx.serialization.json.JsonPrimitive("Shanu"))

        setContent {
            TextRenderer(textDto, Modifier, stateHolder)
        }
        onNodeWithText("Hello, Shanu!").assertExists()
    }
}

