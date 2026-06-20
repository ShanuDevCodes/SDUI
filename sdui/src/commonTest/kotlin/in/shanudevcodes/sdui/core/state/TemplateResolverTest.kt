package `in`.shanudevcodes.sdui.core.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class TemplateResolverTest {

    @Test
    fun testResolve_basicPlaceholder() {
        val state = mapOf(
            "name" to JsonPrimitive("Alice")
        )
        val result = TemplateResolver.resolve("Hello, {{name}}!", state)
        assertEquals("Hello, Alice!", result)
    }

    @Test
    fun testResolve_multiplePlaceholders() {
        val state = mapOf(
            "firstName" to JsonPrimitive("John"),
            "lastName" to JsonPrimitive("Doe")
        )
        val result = TemplateResolver.resolve("{{firstName}} {{lastName}}", state)
        assertEquals("John Doe", result)
    }

    @Test
    fun testResolve_missingKey_returnsEmptyString() {
        val state = emptyMap<String, JsonPrimitive>()
        val result = TemplateResolver.resolve("Hello {{name}}!", state)
        assertEquals("Hello !", result)
    }

    @Test
    fun testResolve_nullValue_returnsEmptyString() {
        val state = mapOf(
            "name" to JsonNull
        )
        val result = TemplateResolver.resolve("Hello {{name}}!", state)
        assertEquals("Hello !", result)
    }

    @Test
    fun testResolve_nonStringPrimitive() {
        val state = mapOf(
            "count" to JsonPrimitive(42),
            "enabled" to JsonPrimitive(true)
        )
        val result = TemplateResolver.resolve("Count is {{count}}, active: {{enabled}}", state)
        assertEquals("Count is 42, active: true", result)
    }

    @Test
    fun testResolve_nestedPlaceholders() {
        val state = mapOf(
            "key" to JsonPrimitive("name"),
            "user_name" to JsonPrimitive("Bob")
        )
        // innermost {{key}} resolves to "name", yielding {{user_name}}, which resolves to "Bob"
        val result = TemplateResolver.resolve("Hello {{user_{{key}}}}!", state)
        assertEquals("Hello Bob!", result)
    }

    @Test
    fun testResolve_recursiveValueResolution() {
        val state = mapOf(
            "name" to JsonPrimitive("Charlie"),
            "greeting" to JsonPrimitive("Welcome {{name}}!")
        )
        val result = TemplateResolver.resolve("{{greeting}}", state)
        assertEquals("Welcome Charlie!", result)
    }

    @Test
    fun testResolve_nestedPaths() {
        val profile = JsonObject(mapOf(
            "first" to JsonPrimitive("Dave"),
            "age" to JsonPrimitive(30)
        ))
        val state = mapOf(
            "user" to profile
        )
        val result = TemplateResolver.resolve("{{user.first}} is {{user.age}} years old.", state)
        assertEquals("Dave is 30 years old.", result)
    }

    @Test
    fun testResolve_circularReference_terminatesSafely() {
        val state = mapOf(
            "a" to JsonPrimitive("{{b}}"),
            "b" to JsonPrimitive("{{a}}")
        )
        val result = TemplateResolver.resolve("{{a}}", state)
        // Verify it terminates safely (it will reach the recursion depth and stop, preventing infinite loops)
        assertEquals(true, result.isNotEmpty())
    }
}
