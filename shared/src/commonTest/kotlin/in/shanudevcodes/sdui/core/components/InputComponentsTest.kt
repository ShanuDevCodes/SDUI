package `in`.shanudevcodes.sdui.core.components

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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
import kotlin.test.assertTrue
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalTestApi::class)
class InputComponentsTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testButtonRenderer_clickDispatchesAction() = runComposeUiTest {
        val onClickAction = SduiActionDto(type = "Navigate", route = "details")
        val buttonDto = SduiComponentDto(
            type = "Button",
            props = mapOf(
                "text" to kotlinx.serialization.json.JsonPrimitive("Click Me")
            ),
            actions = mapOf(
                "onClick" to onClickAction
            )
        )
        val stateHolder = SduiStateHolder()
        var dispatchedAction: SduiActionDto? = null

        setContent {
            CompositionLocalProvider(LocalSduiActionHandler provides { dispatchedAction = it }) {
                ButtonRenderer(buttonDto, Modifier, stateHolder)
            }
        }

        onNodeWithText("Click Me").performClick()
        assertEquals(onClickAction, dispatchedAction)
    }

    @Test
    fun testTextFieldRenderer_updatesState() = runComposeUiTest {
        val textFieldDto = SduiComponentDto(
            type = "TextField",
            props = mapOf(
                "stateKey" to kotlinx.serialization.json.JsonPrimitive("username"),
                "label" to kotlinx.serialization.json.JsonPrimitive("Enter username")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            TextFieldRenderer(textFieldDto, Modifier, stateHolder)
        }

        onNodeWithText("Enter username").performTextInput("alice")
        val stateVal = stateHolder.getValue("username")?.jsonPrimitive?.content
        assertEquals("alice", stateVal)
    }

    @Test
    fun testSwitchRenderer_updatesState() = runComposeUiTest {
        val switchDto = SduiComponentDto(
            type = "Switch",
            props = mapOf(
                "stateKey" to kotlinx.serialization.json.JsonPrimitive("enabled")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            SwitchRenderer(switchDto, Modifier, stateHolder)
        }

        // Initially false
        assertEquals(false, stateHolder.getValue("enabled")?.jsonPrimitive?.booleanOrNull ?: false)

        // Click it
        onNode(hasClickAction()).performClick()

        // Should be true
        assertEquals(true, stateHolder.getValue("enabled")?.jsonPrimitive?.booleanOrNull ?: false)
    }

    @Test
    fun testCheckboxRenderer_updatesState() = runComposeUiTest {
        val checkboxDto = SduiComponentDto(
            type = "Checkbox",
            props = mapOf(
                "stateKey" to kotlinx.serialization.json.JsonPrimitive("agree")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            CheckboxRenderer(checkboxDto, Modifier, stateHolder)
        }

        // Initially false
        assertEquals(false, stateHolder.getValue("agree")?.jsonPrimitive?.booleanOrNull ?: false)

        // Click it
        onNode(hasClickAction()).performClick()

        // Should be true
        assertEquals(true, stateHolder.getValue("agree")?.jsonPrimitive?.booleanOrNull ?: false)
    }

    @Test
    fun testRadioButtonRenderer_updatesState() = runComposeUiTest {
        val radioDto = SduiComponentDto(
            type = "RadioButton",
            props = mapOf(
                "stateKey" to kotlinx.serialization.json.JsonPrimitive("choice"),
                "value" to kotlinx.serialization.json.JsonPrimitive("OptionA")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            RadioButtonRenderer(radioDto, Modifier, stateHolder)
        }

        onNode(hasClickAction()).performClick()

        val value = stateHolder.getValue("choice")?.jsonPrimitive?.content
        assertEquals("OptionA", value)
    }

    @Test
    fun testSliderRenderer_rendersSuccessfully() = runComposeUiTest {
        val sliderDto = SduiComponentDto(
            type = "Slider",
            props = mapOf(
                "stateKey" to kotlinx.serialization.json.JsonPrimitive("volume"),
                "valueRangeMin" to kotlinx.serialization.json.JsonPrimitive(0.0f),
                "valueRangeMax" to kotlinx.serialization.json.JsonPrimitive(100.0f),
                "steps" to kotlinx.serialization.json.JsonPrimitive(4)
            )
        )
        val stateHolder = SduiStateHolder()
        stateHolder.setValue("volume", kotlinx.serialization.json.JsonPrimitive(50.0f))

        setContent {
            SliderRenderer(sliderDto, Modifier, stateHolder)
        }

        assertTrue(true)
    }

    @Test
    fun testDropdownMenuRenderer_selectsOption() = runComposeUiTest {
        val dropdownDto = SduiComponentDto(
            type = "DropdownMenu",
            props = mapOf(
                "stateKey" to kotlinx.serialization.json.JsonPrimitive("fruit"),
                "label" to kotlinx.serialization.json.JsonPrimitive("Select Fruit"),
                "options" to kotlinx.serialization.json.JsonArray(
                    listOf(
                        kotlinx.serialization.json.JsonPrimitive("Apple"),
                        kotlinx.serialization.json.JsonPrimitive("Banana")
                    )
                )
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            DropdownMenuRenderer(dropdownDto, Modifier, stateHolder)
        }

        onNodeWithText("Select Fruit").performClick()
        onNodeWithText("Banana").performClick()

        val selected = stateHolder.getValue("fruit")?.jsonPrimitive?.content
        assertEquals("Banana", selected)
    }
}
