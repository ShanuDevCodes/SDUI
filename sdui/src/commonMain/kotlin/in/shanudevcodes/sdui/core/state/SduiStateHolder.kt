package `in`.shanudevcodes.sdui.core.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.JsonElement

/**
 * State holder per SDUI screen that maintains a reactive map of user input states and variables.
 */
public class SduiStateHolder(initialState: Map<String, JsonElement> = emptyMap()) {
    private val _state = MutableStateFlow(initialState)
    public val state: StateFlow<Map<String, JsonElement>> = _state.asStateFlow()

    public fun getValue(key: String): JsonElement? = _state.value[key]

    public fun setValue(key: String, value: JsonElement) {
        _state.update { current ->
            val next = current.toMutableMap()
            next[key] = value
            next
        }
    }
}
