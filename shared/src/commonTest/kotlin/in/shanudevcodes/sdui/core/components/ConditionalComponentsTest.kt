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
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.serialization.json.JsonPrimitive

@OptIn(ExperimentalTestApi::class)
class ConditionalComponentsTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testConditionEvaluator_operators() {
        val jsonTrue = JsonPrimitive(true)
        val jsonFalse = JsonPrimitive(false)
        val jsonFive = JsonPrimitive(5)
        val jsonTen = JsonPrimitive(10)
        val jsonHello = JsonPrimitive("hello world")

        // Default boolean check (empty operator)
        assertTrue(ConditionEvaluator.evaluate(jsonTrue, "", null))
        assertFalse(ConditionEvaluator.evaluate(jsonFalse, "", null))

        // eq / neq
        assertTrue(ConditionEvaluator.evaluate(jsonFive, "eq", jsonFive))
        assertFalse(ConditionEvaluator.evaluate(jsonFive, "eq", jsonTen))
        assertTrue(ConditionEvaluator.evaluate(jsonFive, "neq", jsonTen))

        // gt / lt
        assertTrue(ConditionEvaluator.evaluate(jsonTen, "gt", jsonFive))
        assertFalse(ConditionEvaluator.evaluate(jsonFive, "gt", jsonTen))
        assertTrue(ConditionEvaluator.evaluate(jsonFive, "lt", jsonTen))

        // contains
        assertTrue(ConditionEvaluator.evaluate(jsonHello, "contains", JsonPrimitive("world")))
        assertFalse(ConditionEvaluator.evaluate(jsonHello, "contains", JsonPrimitive("mars")))

        // isEmpty / isNotEmpty
        assertTrue(ConditionEvaluator.evaluate(JsonPrimitive(""), "isEmpty", null))
        assertFalse(ConditionEvaluator.evaluate(JsonPrimitive("text"), "isEmpty", null))
        assertTrue(ConditionEvaluator.evaluate(JsonPrimitive("text"), "isNotEmpty", null))
        assertFalse(ConditionEvaluator.evaluate(JsonPrimitive(""), "isNotEmpty", null))

        // Type normalization equality tests (eq / neq)
        assertTrue(ConditionEvaluator.evaluate(JsonPrimitive(true), "eq", JsonPrimitive("true")))
        assertTrue(ConditionEvaluator.evaluate(JsonPrimitive("true"), "eq", JsonPrimitive(true)))
        assertTrue(ConditionEvaluator.evaluate(JsonPrimitive(1), "eq", JsonPrimitive(1.0f)))
        assertTrue(ConditionEvaluator.evaluate(JsonPrimitive(1.0f), "eq", JsonPrimitive("1.0")))
        assertTrue(ConditionEvaluator.evaluate(JsonPrimitive("hello"), "eq", JsonPrimitive("hello")))
        assertFalse(ConditionEvaluator.evaluate(JsonPrimitive(true), "eq", JsonPrimitive("false")))
        assertFalse(ConditionEvaluator.evaluate(JsonPrimitive(1), "eq", JsonPrimitive(2)))
        assertTrue(ConditionEvaluator.evaluate(JsonPrimitive(1), "neq", JsonPrimitive(2)))
    }

    @Test
    fun testConditionalRenderer_rendersThenBranchWhenTrue() = runComposeUiTest {
        var thenRendered = 0
        var elseRendered = 0
        ComponentRegistry.register("ThenType") { _, _, _ -> thenRendered++ }
        ComponentRegistry.register("ElseType") { _, _, _ -> elseRendered++ }

        val conditionalDto = SduiComponentDto(
            type = "Conditional",
            props = mapOf(
                "stateKey" to JsonPrimitive("loggedIn"),
                "operator" to JsonPrimitive("eq"),
                "compareValue" to JsonPrimitive(true),
                "then" to kotlinx.serialization.json.Json.encodeToJsonElement(
                    SduiComponentDto.serializer(),
                    SduiComponentDto(type = "ThenType")
                ),
                "else" to kotlinx.serialization.json.Json.encodeToJsonElement(
                    SduiComponentDto.serializer(),
                    SduiComponentDto(type = "ElseType")
                )
            )
        )
        val stateHolder = SduiStateHolder()
        stateHolder.setValue("loggedIn", JsonPrimitive(true))

        setContent {
            ConditionalRenderer(conditionalDto, Modifier, stateHolder)
        }

        assertEquals(1, thenRendered)
        assertEquals(0, elseRendered)
    }

    @Test
    fun testConditionalRenderer_rendersElseBranchWhenFalse() = runComposeUiTest {
        var thenRendered = 0
        var elseRendered = 0
        ComponentRegistry.register("ThenType") { _, _, _ -> thenRendered++ }
        ComponentRegistry.register("ElseType") { _, _, _ -> elseRendered++ }

        val conditionalDto = SduiComponentDto(
            type = "Conditional",
            props = mapOf(
                "stateKey" to JsonPrimitive("loggedIn"),
                "operator" to JsonPrimitive("eq"),
                "compareValue" to JsonPrimitive(true)
            ),
            children = listOf(
                SduiComponentDto(type = "ThenType"),
                SduiComponentDto(type = "ElseType")
            )
        )
        val stateHolder = SduiStateHolder()
        stateHolder.setValue("loggedIn", JsonPrimitive(false))

        setContent {
            ConditionalRenderer(conditionalDto, Modifier, stateHolder)
        }

        assertEquals(0, thenRendered)
        assertEquals(1, elseRendered)
    }

    @Test
    fun testVisibleRenderer_rendersChildrenOnlyWhenTrue() = runComposeUiTest {
        var childRendered = 0
        ComponentRegistry.register("ChildType") { _, _, _ -> childRendered++ }

        val visibleDto = SduiComponentDto(
            type = "Visible",
            props = mapOf(
                "stateKey" to JsonPrimitive("showBanner"),
                "operator" to JsonPrimitive("eq"),
                "compareValue" to JsonPrimitive(true)
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType")
            )
        )
        val stateHolder = SduiStateHolder()

        setContent {
            VisibleRenderer(visibleDto, Modifier, stateHolder)
        }
        assertEquals(0, childRendered)
    }

    @Test
    fun testVisibleRenderer_rendersWhenTrueFresh() = runComposeUiTest {
        var childRendered = 0
        ComponentRegistry.register("ChildType") { _, _, _ -> childRendered++ }

        val visibleDto = SduiComponentDto(
            type = "Visible",
            props = mapOf(
                "stateKey" to JsonPrimitive("showBanner"),
                "operator" to JsonPrimitive("eq"),
                "compareValue" to JsonPrimitive(true)
            ),
            children = listOf(
                SduiComponentDto(type = "ChildType")
            )
        )
        val stateHolder = SduiStateHolder()
        stateHolder.setValue("showBanner", JsonPrimitive(true))

        setContent {
            VisibleRenderer(visibleDto, Modifier, stateHolder)
        }
        assertEquals(1, childRendered)
    }
}
