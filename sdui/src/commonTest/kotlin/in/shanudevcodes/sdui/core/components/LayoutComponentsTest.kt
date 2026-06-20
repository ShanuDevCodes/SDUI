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
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class LayoutComponentsTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testColumnRenderer_rendersChildren() = runComposeUiTest {
        var childRenderCount = 0
        ComponentRegistry.register("ChildType") { _, _, _ ->
            childRenderCount++
        }

        val columnDto = SduiComponentDto(
            type = "Column",
            props = mapOf(
                "verticalArrangement" to kotlinx.serialization.json.JsonPrimitive("SpaceBetween"),
                "horizontalAlignment" to kotlinx.serialization.json.JsonPrimitive("Center")
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType")
            )
        )

        val stateHolder = SduiStateHolder()

        setContent {
            ColumnRenderer(columnDto, Modifier, stateHolder)
        }

        assertEquals(2, childRenderCount, "Column should render all its children")
    }

    @Test
    fun testRowRenderer_rendersChildren() = runComposeUiTest {
        var childRenderCount = 0
        ComponentRegistry.register("ChildType") { _, _, _ ->
            childRenderCount++
        }

        val rowDto = SduiComponentDto(
            type = "Row",
            props = mapOf(
                "space" to kotlinx.serialization.json.JsonPrimitive(8),
                "verticalAlignment" to kotlinx.serialization.json.JsonPrimitive("Center")
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType")
            )
        )

        val stateHolder = SduiStateHolder()

        setContent {
            RowRenderer(rowDto, Modifier, stateHolder)
        }

        assertEquals(1, childRenderCount, "Row should render all its children")
    }

    @Test
    fun testBoxRenderer_rendersChildren() = runComposeUiTest {
        var childRenderCount = 0
        ComponentRegistry.register("ChildType") { _, _, _ ->
            childRenderCount++
        }

        val boxDto = SduiComponentDto(
            type = "Box",
            props = mapOf(
                "contentAlignment" to kotlinx.serialization.json.JsonPrimitive("BottomEnd")
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType")
            )
        )

        val stateHolder = SduiStateHolder()

        setContent {
            BoxRenderer(boxDto, Modifier, stateHolder)
        }

        assertEquals(2, childRenderCount, "Box should render all its children")
    }

    @Test
    fun testSpacerRenderer_doesNotThrow() = runComposeUiTest {
        val spacerDto = SduiComponentDto(type = "Spacer")
        val stateHolder = SduiStateHolder()

        setContent {
            SpacerRenderer(spacerDto, Modifier, stateHolder)
        }
        assertTrue(true)
    }
}
