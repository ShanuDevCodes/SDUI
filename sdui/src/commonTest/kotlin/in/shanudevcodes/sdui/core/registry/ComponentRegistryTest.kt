package `in`.shanudevcodes.sdui.core.registry

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ComponentRegistryTest {

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testBuiltInComponents_registeredByDefault() {
        assertNotNull(ComponentRegistry.resolve("Column"), "Column should be registered")
        assertNotNull(ComponentRegistry.resolve("Row"), "Row should be registered")
        assertNotNull(ComponentRegistry.resolve("Text"), "Text should be registered")
        assertNotNull(ComponentRegistry.resolve("Button"), "Button should be registered")
    }

    @Test
    fun testResolve_unknownComponent_returnsNull() {
        assertNull(ComponentRegistry.resolve("UnregisteredComponent"), "Unknown component must resolve to null")
    }

    @Test
    fun testRegister_customComponent_resolvesSuccessfully() {
        val customRenderer: ComponentRenderer = { _, _, _ -> }

        ComponentRegistry.register("CustomCard", customRenderer)
        val resolved = ComponentRegistry.resolve("CustomCard")

        assertNotNull(resolved, "Custom component should resolve")
        assertTrue(resolved === customRenderer, "Resolved renderer must match registered instance")
    }
}
