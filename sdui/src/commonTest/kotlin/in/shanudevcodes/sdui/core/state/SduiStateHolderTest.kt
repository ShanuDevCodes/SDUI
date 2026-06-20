package `in`.shanudevcodes.sdui.core.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.serialization.json.JsonPrimitive

/**
 * Unit tests for SduiStateHolder class.
 */
class SduiStateHolderTest {

    @Test
    fun testInitialization_andGetValue() {
        val initialState = mapOf(
            "username" to JsonPrimitive("bob"),
            "age" to JsonPrimitive(30)
        )
        val stateHolder = SduiStateHolder(initialState)

        assertEquals(JsonPrimitive("bob"), stateHolder.getValue("username"))
        assertEquals(JsonPrimitive(30), stateHolder.getValue("age"))
        assertNull(stateHolder.getValue("nonexistent"))
    }

    @Test
    fun testSetValue_updatesStateAndFlow() {
        val stateHolder = SduiStateHolder()
        assertNull(stateHolder.getValue("count"))

        stateHolder.setValue("count", JsonPrimitive(1))
        assertEquals(JsonPrimitive(1), stateHolder.getValue("count"))

        val currentMap = stateHolder.state.value
        assertEquals(JsonPrimitive(1), currentMap["count"])
    }
}
