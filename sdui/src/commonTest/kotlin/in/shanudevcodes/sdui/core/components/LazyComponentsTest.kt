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
class LazyComponentsTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testLazyColumnRenderer_rendersChildren() = runComposeUiTest {
        var childRenderCount = 0
        ComponentRegistry.register("ChildType") { _, _, _ ->
            childRenderCount++
        }

        val lazyColumnDto = SduiComponentDto(
            type = "LazyColumn",
            props = mapOf(
                "space" to kotlinx.serialization.json.JsonPrimitive(8)
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            LazyColumnRenderer(lazyColumnDto, Modifier, stateHolder)
        }

        assertEquals(3, childRenderCount, "LazyColumn should render all its children")
    }

    @Test
    fun testLazyRowRenderer_rendersChildren() = runComposeUiTest {
        var childRenderCount = 0
        ComponentRegistry.register("ChildType") { _, _, _ ->
            childRenderCount++
        }

        val lazyRowDto = SduiComponentDto(
            type = "LazyRow",
            props = mapOf(
                "space" to kotlinx.serialization.json.JsonPrimitive(4)
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            LazyRowRenderer(lazyRowDto, Modifier, stateHolder)
        }

        assertEquals(2, childRenderCount, "LazyRow should render all its children")
    }

    @Test
    fun testLazyGridRenderer_rendersChildren() = runComposeUiTest {
        var childRenderCount = 0
        ComponentRegistry.register("ChildType") { _, _, _ ->
            childRenderCount++
        }

        val lazyGridDto = SduiComponentDto(
            type = "LazyGrid",
            props = mapOf(
                "columns" to kotlinx.serialization.json.JsonPrimitive(3),
                "space" to kotlinx.serialization.json.JsonPrimitive(8)
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType"),
                SduiComponentDto(type = "ChildType")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            LazyGridRenderer(lazyGridDto, Modifier, stateHolder)
        }

        assertEquals(4, childRenderCount, "LazyGrid should render all its children")
    }
}

