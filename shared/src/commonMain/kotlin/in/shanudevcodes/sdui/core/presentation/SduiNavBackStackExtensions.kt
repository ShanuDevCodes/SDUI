package `in`.shanudevcodes.sdui.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import androidx.navigation3.runtime.NavBackStack

/**
 * Extension on [NavBackStack] to observe whether a specific [SduiRoute] is currently at the top of the stack.
 */
@Composable
fun NavBackStack<SduiRoute>.isTopAsState(route: SduiRoute): State<Boolean> {
    return produceState(initialValue = lastOrNull() == route, this, route) {
        snapshotFlow { lastOrNull() }
            .collect { top ->
                value = top == route
            }
    }
}

/**
 * Navigates to a tab or base route by clearing intermediate screens.
 */
fun NavBackStack<SduiRoute>.navigateTab(route: SduiRoute) {
    if (lastOrNull() == route) return
    while (size > 1) {
        removeAt(size - 1)
    }
    if (lastOrNull() != route) {
        add(route)
    }
}
