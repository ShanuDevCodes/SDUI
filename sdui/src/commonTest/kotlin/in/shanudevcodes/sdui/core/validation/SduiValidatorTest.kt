package `in`.shanudevcodes.sdui.core.validation

import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.SduiScreenDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SduiValidatorTest {

    @Test
    fun testValidator_validScreen() {
        val root = SduiComponentDto(type = "Text")
        val screen = SduiScreenDto(
            screenId = "home",
            schemaVersion = "1.0.0",
            root = root
        )

        val result = SduiValidator.validate(screen)
        assertTrue(result is SduiValidator.ValidationResult.Valid)
    }

    @Test
    fun testValidator_incompatibleVersion() {
        val root = SduiComponentDto(type = "Text")
        val screen = SduiScreenDto(
            screenId = "home",
            schemaVersion = "1.1.0",
            root = root
        )

        val result = SduiValidator.validate(screen)
        assertTrue(result is SduiValidator.ValidationResult.Invalid)
        assertTrue((result as SduiValidator.ValidationResult.Invalid).reason.contains("exceeds max supported"))
    }

    @Test
    fun testValidator_blankRootType() {
        val root = SduiComponentDto(type = " ")
        val screen = SduiScreenDto(
            screenId = "home",
            schemaVersion = "1.0.0",
            root = root
        )

        val result = SduiValidator.validate(screen)
        assertTrue(result is SduiValidator.ValidationResult.Invalid)
        assertEquals("Root component has no type", (result as SduiValidator.ValidationResult.Invalid).reason)
    }

    @Test
    fun testValidator_excessiveDepth() {
        // Create a tree of depth 51
        var current = SduiComponentDto(type = "Text")
        for (i in 1..51) {
            current = SduiComponentDto(
                type = "Column",
                children = listOf(current)
            )
        }
        val screen = SduiScreenDto(
            screenId = "home",
            schemaVersion = "1.0.0",
            root = current
        )

        val result = SduiValidator.validate(screen)
        assertTrue(result is SduiValidator.ValidationResult.Invalid)
        assertEquals("Component tree exceeds max depth of 50", (result as SduiValidator.ValidationResult.Invalid).reason)
    }
}
