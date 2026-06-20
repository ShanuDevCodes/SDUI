package `in`.shanudevcodes.sdui.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import `in`.shanudevcodes.sdui.feature.screen.domain.repository.ScreenRepository
import `in`.shanudevcodes.sdui.feature.screen.presentation.SduiScreenComposable
import `in`.shanudevcodes.sdui.feature.screen.presentation.SduiScreenViewModel

/**
 * Navigation 3 Display container preconfigured to dynamically render server-driven UI screens.
 *
 * @param backStack The back stack containing [SduiRoute] destinations.
 * @param repository The repository implementation used to fetch screen payloads.
 * @param onNavigate Callback invoked when a navigation event is triggered from SDUI.
 * @param onShowSnackbar Callback invoked when a snackbar notification is requested.
 * @param modifier Modifier for the layout.
 */
@Composable
public fun SduiNavDisplay(
    backStack: NavBackStack<SduiRoute>,
    repository: ScreenRepository,
    onNavigate: (route: String, params: Map<String, String>) -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<SduiRoute.Screen> { route ->
                val viewModel = remember(route.screenId) {
                    SduiScreenViewModel(screenId = route.screenId, repository = repository)
                }
                SduiScreenComposable(
                    viewModel = viewModel,
                    onNavigate = onNavigate,
                    onShowSnackbar = onShowSnackbar,
                    modifier = modifier
                )
            }
        }
    )
}
