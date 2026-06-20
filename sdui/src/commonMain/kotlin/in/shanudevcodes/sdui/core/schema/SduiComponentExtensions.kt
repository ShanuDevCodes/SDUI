package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Extension helper to read a string property from the component props.
 */
public fun SduiComponentDto.stringProp(key: String, default: String = ""): String {
    return props[key]?.jsonPrimitive?.content ?: default
}

/**
 * Extension helper to read an integer property from the component props.
 */
public fun SduiComponentDto.intProp(key: String, default: Int = 0): Int {
    return props[key]?.jsonPrimitive?.intOrNull ?: default
}

/**
 * Extension helper to read a boolean property from the component props.
 */
public fun SduiComponentDto.booleanProp(key: String, default: Boolean = false): Boolean {
    return props[key]?.jsonPrimitive?.booleanOrNull ?: default
}

/**
 * Extension helper to retrieve an action by key.
 */
public fun SduiComponentDto.action(key: String): SduiActionDto? {
    return actions[key]
}

/**
 * Extension helper to read a string property and resolve {{template}} placeholders against state.
 */
public fun SduiComponentDto.resolvedStringProp(key: String, state: Map<String, kotlinx.serialization.json.JsonElement>, default: String = ""): String {
    val raw = stringProp(key, default)
    return `in`.shanudevcodes.sdui.core.state.TemplateResolver.resolve(raw, state)
}

/**
 * Extension helper to retrieve and decode style property from props.
 */
internal fun SduiComponentDto.styleProp(): SduiStyleDto? {
    val element = props["style"] ?: return null
    return try {
        kotlinx.serialization.json.Json.decodeFromJsonElement(SduiStyleDto.serializer(), element)
    } catch (e: Exception) {
        null
    }
}

