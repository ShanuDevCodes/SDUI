package `in`.shanudevcodes.sdui.core.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.JsonElement

/**
 * State holder per SDUI screen that maintains a reactive map of user input states and variables.
 */
class SduiStateHolder(initialState: Map<String, JsonElement> = emptyMap()) {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<Map<String, JsonElement>> = _state.asStateFlow()

    /**
     * Retrieves a state value by key.
     */
    fun getValue(key: String): JsonElement? {
        return _state.value[key]
    }

    /**
     * Updates/sets a state value, notifying observers.
     */
    fun setValue(key: String, value: JsonElement) {
        _state.update { current ->
            val next = current.toMutableMap()
            next[key] = value
            next
        }
    }
}
