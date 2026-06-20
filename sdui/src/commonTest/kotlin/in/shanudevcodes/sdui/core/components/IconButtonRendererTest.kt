package `in`.shanudevcodes.sdui.core.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalTestApi::class)
class IconButtonRendererTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testIconButtonRenderer_clickAndDescription() = runComposeUiTest {
        val onClickAction = SduiActionDto(type = "Navigate", route = "details")
        val iconButtonDto = SduiComponentDto(
            type = "IconButton",
            props = mapOf(
                "icon" to kotlinx.serialization.json.JsonPrimitive("favorite"),
                "contentDescription" to kotlinx.serialization.json.JsonPrimitive("Like Button"),
                "enabled" to kotlinx.serialization.json.JsonPrimitive(true)
            ),
            actions = mapOf("onClick" to onClickAction)
        )
        val stateHolder = SduiStateHolder()
        var dispatchedAction: SduiActionDto? = null

        setContent {
            androidx.compose.runtime.CompositionLocalProvider(
                LocalSduiActionHandler provides { dispatchedAction = it }
            ) {
                IconButtonRenderer(iconButtonDto, Modifier, stateHolder)
            }
        }

        onNode(hasContentDescription("Like Button")).performClick()
        assertNotNull(dispatchedAction)
        assertEquals(onClickAction, dispatchedAction)
    }

    @Test
    fun testIconButtonRenderer_disabledDoesNotClick() = runComposeUiTest {
        val onClickAction = SduiActionDto(type = "Navigate", route = "details")
        val iconButtonDto = SduiComponentDto(
            type = "IconButton",
            props = mapOf(
                "icon" to kotlinx.serialization.json.JsonPrimitive("favorite"),
                "contentDescription" to kotlinx.serialization.json.JsonPrimitive("Like Button"),
                "enabled" to kotlinx.serialization.json.JsonPrimitive(false)
            ),
            actions = mapOf("onClick" to onClickAction)
        )
        val stateHolder = SduiStateHolder()
        var dispatchedAction: SduiActionDto? = null

        setContent {
            androidx.compose.runtime.CompositionLocalProvider(
                LocalSduiActionHandler provides { dispatchedAction = it }
            ) {
                IconButtonRenderer(iconButtonDto, Modifier, stateHolder)
            }
        }

        // Try to click, but since it is disabled, performClick should fail or action should be null.
        try {
            onNode(hasContentDescription("Like Button")).performClick()
        } catch (e: AssertionError) {
            // Safe to ignore: some platforms throw if node is disabled/unclickable
        }
        assertNull(dispatchedAction)
    }
}
