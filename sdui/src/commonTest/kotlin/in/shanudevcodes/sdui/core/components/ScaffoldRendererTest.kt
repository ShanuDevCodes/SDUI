package `in`.shanudevcodes.sdui.core.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
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

@OptIn(ExperimentalTestApi::class)
class ScaffoldRendererTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testScaffoldRenderer_rendersTopBarAndChildren() = runComposeUiTest {
        ComponentRegistry.register("ChildText") { comp, _, _ ->
            androidx.compose.material3.Text(comp.props["text"]?.toString() ?: "")
        }

        val scaffoldDto = SduiComponentDto(
            type = "Scaffold",
            props = mapOf(
                "topBarTitle" to kotlinx.serialization.json.JsonPrimitive("My Scaffold"),
                "showTopBar" to kotlinx.serialization.json.JsonPrimitive(true),
                "topBarNavigationIcon" to kotlinx.serialization.json.JsonPrimitive("back")
            ),
            children = listOf(
                SduiComponentDto(
                    type = "ChildText",
                    props = mapOf("text" to kotlinx.serialization.json.JsonPrimitive("Hello Inside Scaffold"))
                )
            )
        )
        val stateHolder = SduiStateHolder()
        var dispatchedAction: SduiActionDto? = null

        setContent {
            androidx.compose.runtime.CompositionLocalProvider(
                LocalSduiActionHandler provides { dispatchedAction = it }
            ) {
                ScaffoldRenderer(scaffoldDto, Modifier, stateHolder)
            }
        }

        onNodeWithText("My Scaffold").assertExists()
        onNodeWithText("\"Hello Inside Scaffold\"").assertExists()

        // Test navigation click triggers back action
        onNodeWithContentDescription("back").performClick()
        assertNotNull(dispatchedAction)
        assertEquals("Navigate", dispatchedAction?.type)
        assertEquals("back", dispatchedAction?.route)
    }
}

