package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Extension helper to read a string property from the component props.
 */
fun SduiComponentDto.stringProp(key: String, default: String = ""): String {
    return props[key]?.jsonPrimitive?.content ?: default
}

/**
 * Extension helper to read an integer property from the component props.
 */
fun SduiComponentDto.intProp(key: String, default: Int = 0): Int {
    return props[key]?.jsonPrimitive?.intOrNull ?: default
}

/**
 * Extension helper to read a boolean property from the component props.
 */
fun SduiComponentDto.booleanProp(key: String, default: Boolean = false): Boolean {
    return props[key]?.jsonPrimitive?.booleanOrNull ?: default
}

/**
 * Extension helper to retrieve an action by key.
 */
fun SduiComponentDto.action(key: String): SduiActionDto? {
    return actions[key]
}
