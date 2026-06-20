package `in`.shanudevcodes.sdui.navigation

import `in`.shanudevcodes.sdui.core.presentation.SduiRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SduiNavigatorTest {

    @Test
    fun testInitialization() {
        val root = SduiRoute.Screen("home")
        val navigator = SduiNavigator(root)

        assertEquals(listOf(root), navigator.backstack.value)
    }

    @Test
    fun testPush() {
        val root = SduiRoute.Screen("home")
        val screen2 = SduiRoute.Screen("details")
        val navigator = SduiNavigator(root)

        navigator.push(screen2)

        assertEquals(listOf(root, screen2), navigator.backstack.value)
    }

    @Test
    fun testPop_withMultipleScreens() {
        val root = SduiRoute.Screen("home")
        val screen2 = SduiRoute.Screen("details")
        val navigator = SduiNavigator(root)
        navigator.push(screen2)

        val popped = navigator.pop()

        assertTrue(popped)
        assertEquals(listOf(root), navigator.backstack.value)
    }

    @Test
    fun testPop_withSingleScreen_fails() {
        val root = SduiRoute.Screen("home")
        val navigator = SduiNavigator(root)

        val popped = navigator.pop()

        assertFalse(popped)
        assertEquals(listOf(root), navigator.backstack.value)
    }

    @Test
    fun testReplace() {
        val root = SduiRoute.Screen("home")
        val screen2 = SduiRoute.Screen("details")
        val screen3 = SduiRoute.Screen("settings")

        val navigator = SduiNavigator(root)
        navigator.push(screen2)

        navigator.replace(screen3)

        assertEquals(listOf(root, screen3), navigator.backstack.value)
    }

    @Test
    fun testPopToRoot() {
        val root = SduiRoute.Screen("home")
        val screen2 = SduiRoute.Screen("details")
        val screen3 = SduiRoute.Screen("settings")

        val navigator = SduiNavigator(root)
        navigator.push(screen2)
        navigator.push(screen3)

        navigator.popToRoot()

        assertEquals(listOf(root), navigator.backstack.value)
    }
}
