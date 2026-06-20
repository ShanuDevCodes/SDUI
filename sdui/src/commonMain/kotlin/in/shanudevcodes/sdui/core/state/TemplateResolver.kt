package `in`.shanudevcodes.sdui.core.state

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Utility to resolve `{{stateKey}}` placeholders inside strings using state values.
 */
internal object TemplateResolver {

    private val placeholderRegex = Regex("\\{\\{([^{}]+)\\}\\}")
    private const val MAX_RECURSION_DEPTH = 10

    /**
     * Resolves all placeholders in the given string template using the state map.
     * Replaces missing keys or null values with an empty string.
     */
    fun resolve(template: String, state: Map<String, JsonElement>): String {
        var result = template
        var depth = 0

        while (result.contains("{{") && result.contains("}}") && depth < MAX_RECURSION_DEPTH) {
            val nextResult = placeholderRegex.replace(result) { matchResult ->
                val path = matchResult.groupValues[1].trim()
                val value = getValueByPath(path, state)
                value?.let { jsonElementToString(it) } ?: ""
            }

            if (nextResult == result) {
                break // No placeholders were resolved or structure did not change
            }

            result = nextResult
            depth++
        }

        return result
    }

    /**
     * Resolves a state value by traversing dotted paths (e.g. "user.profile.name") in a nested JSON structure.
     */
    private fun getValueByPath(path: String, state: Map<String, JsonElement>): JsonElement? {
        // Try direct lookup first
        state[path]?.let { return it }

        // Try nested lookup if it contains '.'
        val parts = path.split('.')
        if (parts.size <= 1) return null

        var current: JsonElement? = state[parts[0]]
        for (i in 1 until parts.size) {
            if (current == null) return null
            if (current is JsonObject) {
                current = current[parts[i]]
            } else {
                return null
            }
        }
        return current
    }

    /**
     * Converts a JsonElement to its string representation for rendering.
     */
    private fun jsonElementToString(element: JsonElement): String {
        return when (element) {
            is JsonNull -> ""
            is JsonPrimitive -> {
                if (element.isString) {
                    element.content
                } else {
                    element.content
                }
            }
            else -> element.toString()
        }
    }
}
