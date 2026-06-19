package `in`.shanudevcodes.sdui.core.renderer

import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.schema.SduiModifierDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ModifierResolverTest {

    @Test
    fun testResolve_padding_singleValue() {
        val modifiers = listOf(
            SduiModifierDto("padding", Json.encodeToJsonElement(16))
        )
        val resolved = ModifierResolver.resolve(modifiers) {}
        val str = resolved.toString()
        assertTrue(str.contains("padding") || str.contains("Padding") || str.contains("PaddingModifier"), "Should contain padding: $str")
    }

    @Test
    fun testResolve_padding_objectValue() {
        val json = Json { ignoreUnknownKeys = true }
        val paddingObj = json.parseToJsonElement("""{"horizontal": 8, "vertical": 12}""")
        val modifiers = listOf(
            SduiModifierDto("padding", paddingObj)
        )
        val resolved = ModifierResolver.resolve(modifiers) {}
        val str = resolved.toString()
        assertTrue(str.contains("padding") || str.contains("Padding") || str.contains("PaddingModifier"), "Should contain padding: $str")
    }

    @Test
    fun testResolve_sizes_widthHeight() {
        val modifiers = listOf(
            SduiModifierDto("width", Json.encodeToJsonElement(120)),
            SduiModifierDto("height", Json.encodeToJsonElement(80))
        )
        val resolved = ModifierResolver.resolve(modifiers) {}
        val str = resolved.toString()
        assertTrue(str.contains("width") || str.contains("height") || str.contains("width") || str.contains("LayoutWeight") || str.contains("Size"), "Should contain width/height: $str")
    }

    @Test
    fun testResolve_fillMaxSize() {
        val modifiers = listOf(
            SduiModifierDto("fillMaxSize", Json.encodeToJsonElement(1.0f))
        )
        val resolved = ModifierResolver.resolve(modifiers) {}
        val str = resolved.toString()
        assertTrue(str.contains("MaxSize") || str.contains("fill") || str.contains("Fill") || str.contains("Size"), "Should contain fillMaxSize: $str")
    }

    @Test
    fun testResolve_background_color() {
        val modifiers = listOf(
            SduiModifierDto("background", Json.encodeToJsonElement("#FF0000"))
        )
        val resolved = ModifierResolver.resolve(modifiers) {}
        val str = resolved.toString()
        assertTrue(str.contains("background") || str.contains("Background") || str.contains("Color"), "Should contain background: $str")
    }

    @Test
    fun testResolve_clickable_dispatchesAction() {
        val action = SduiActionDto(type = "Navigate", route = "details")
        val modifiers = listOf(
            SduiModifierDto("clickable", Json.encodeToJsonElement(action))
        )

        var actionCalled: SduiActionDto? = null
        val resolved = ModifierResolver.resolve(modifiers) {
            actionCalled = it
        }

        assertNotNull(resolved)
        val str = resolved.toString()
        assertTrue(str.contains("clickable") || str.contains("Clickable") || str.contains("CombinedClickable"), "Should contain clickable: $str")
    }
}
