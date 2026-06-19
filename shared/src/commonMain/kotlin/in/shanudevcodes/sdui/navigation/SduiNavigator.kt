package `in`.shanudevcodes.sdui.navigation

import `in`.shanudevcodes.sdui.core.presentation.SduiRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Controller that manages the SDUI navigation backstack.
 */
class SduiNavigator(initialRoute: SduiRoute) {

    private val _backstack = MutableStateFlow(listOf(initialRoute))
    val backstack: StateFlow<List<SduiRoute>> = _backstack.asStateFlow()

    /**
     * Pushes a new route onto the stack.
     */
    fun push(route: SduiRoute) {
        _backstack.update { current ->
            current + route
        }
    }

    /**
     * Pops the top route from the stack.
     * Returns true if a route was successfully popped, false if it could not be popped (e.g. only one element left).
     */
    fun pop(): Boolean {
        var popped = false
        _backstack.update { current ->
            if (current.size > 1) {
                popped = true
                current.dropLast(1)
            } else {
                current
            }
        }
        return popped
    }

    /**
     * Replaces the top route on the stack with the new route.
     */
    fun replace(route: SduiRoute) {
        _backstack.update { current ->
            if (current.isNotEmpty()) {
                current.dropLast(1) + route
            } else {
                listOf(route)
            }
        }
    }

    /**
     * Pops all screens above the root, leaving only the root screen on the stack.
     */
    fun popToRoot() {
        _backstack.update { current ->
            if (current.isNotEmpty()) {
                listOf(current.first())
            } else {
                current
            }
        }
    }
}
