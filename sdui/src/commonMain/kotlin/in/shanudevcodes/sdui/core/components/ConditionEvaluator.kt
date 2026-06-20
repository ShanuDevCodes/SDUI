package `in`.shanudevcodes.sdui.core.components

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Utility to evaluate conditional logic for SDUI components.
 */
internal object ConditionEvaluator {

    /**
     * Evaluates a condition given the state value, operator, and comparison value.
     */
    fun evaluate(
        stateValue: JsonElement?,
        operator: String,
        compareValue: JsonElement?
    ): Boolean {
        val actualValue = stateValue ?: JsonNull

        if (operator.isEmpty()) {
            return actualValue is JsonPrimitive && actualValue.booleanOrNull == true
        }

        return when (operator) {
            "eq" -> normalizedEquals(actualValue, compareValue)
            "neq" -> !normalizedEquals(actualValue, compareValue)
            "gt" -> compareNumeric(actualValue, compareValue) > 0
            "lt" -> compareNumeric(actualValue, compareValue) < 0
            "contains" -> {
                val actStr = actualValue.jsonPrimitiveOrNull()?.content ?: ""
                val compStr = compareValue?.jsonPrimitiveOrNull()?.content ?: ""
                actStr.contains(compStr)
            }
            "isEmpty" -> {
                when (actualValue) {
                    is JsonNull -> true
                    is JsonPrimitive -> actualValue.content.isEmpty()
                    else -> false
                }
            }
            "isNotEmpty" -> {
                when (actualValue) {
                    is JsonNull -> false
                    is JsonPrimitive -> actualValue.content.isNotEmpty()
                    else -> true
                }
            }
            else -> false
        }
    }

    private fun normalizedEquals(a: JsonElement?, b: JsonElement?): Boolean {
        // Both null / JsonNull
        if ((a == null || a is JsonNull) && (b == null || b is JsonNull)) return true
        if (a == null || a is JsonNull || b == null || b is JsonNull) return false

        val aPrim = a.jsonPrimitiveOrNull() ?: return a == b
        val bPrim = b.jsonPrimitiveOrNull() ?: return a == b

        // Boolean coercion: compare as booleans if either side is boolean
        val aBool = aPrim.booleanOrNull ?: aPrim.content.toBooleanStrictOrNull()
        val bBool = bPrim.booleanOrNull ?: bPrim.content.toBooleanStrictOrNull()
        if (aBool != null && bBool != null) return aBool == bBool
        if (aBool != null) {
            val bFromStr = bPrim.content.toBooleanStrictOrNull()
            if (bFromStr != null) return aBool == bFromStr
        }
        if (bBool != null) {
            val aFromStr = aPrim.content.toBooleanStrictOrNull()
            if (aFromStr != null) return bBool == aFromStr
        }

        // Numeric coercion
        val aFloat = aPrim.floatOrNull
        val bFloat = bPrim.floatOrNull
        if (aFloat != null && bFloat != null) return aFloat == bFloat

        // Content string comparison
        return aPrim.content == bPrim.content
    }

    private fun JsonElement.jsonPrimitiveOrNull(): JsonPrimitive? {
        return try {
            this.jsonPrimitive
        } catch (e: Exception) {
            null
        }
    }

    private fun compareNumeric(a: JsonElement, b: JsonElement?): Int {
        if (b == null) return 0
        val aVal = a.jsonPrimitiveOrNull()?.floatOrNull ?: return 0
        val bVal = b.jsonPrimitiveOrNull()?.floatOrNull ?: return 0
        return aVal.compareTo(bVal)
    }
}
