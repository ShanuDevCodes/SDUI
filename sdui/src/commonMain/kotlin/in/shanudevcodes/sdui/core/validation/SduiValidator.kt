package `in`.shanudevcodes.sdui.core.validation

import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.SduiScreenDto

/**
 * Validates a SduiScreenDto against supported schema version and structural constraints.
 */
internal object SduiValidator {
    const val MAX_SUPPORTED_VERSION = "1.0.0"

    sealed interface ValidationResult {
        object Valid : ValidationResult
        data class Invalid(val reason: String) : ValidationResult
    }

    fun validate(dto: SduiScreenDto): ValidationResult {
        if (!isVersionCompatible(dto.schemaVersion, MAX_SUPPORTED_VERSION)) {
            return ValidationResult.Invalid("Schema version ${dto.schemaVersion} exceeds max supported $MAX_SUPPORTED_VERSION")
        }
        if (dto.root.type.isBlank()) {
            return ValidationResult.Invalid("Root component has no type")
        }
        if (componentDepth(dto.root) > 50) {
            return ValidationResult.Invalid("Component tree exceeds max depth of 50")
        }
        return ValidationResult.Valid
    }

    private fun isVersionCompatible(version: String, maxVersion: String): Boolean {
        val v = version.split(".").mapNotNull { it.toIntOrNull() }
        val m = maxVersion.split(".").mapNotNull { it.toIntOrNull() }
        for (i in 0 until maxOf(v.size, m.size)) {
            val vi = v.getOrElse(i) { 0 }
            val mi = m.getOrElse(i) { 0 }
            if (vi > mi) return false
            if (vi < mi) return true
        }
        return true
    }

    private fun componentDepth(component: SduiComponentDto, current: Int = 0): Int {
        if (current > 50) return current
        return component.children.maxOfOrNull { componentDepth(it, current + 1) } ?: current
    }
}
