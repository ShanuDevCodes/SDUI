package `in`.shanudevcodes.sdui.feature.screen.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.coroutines.flow.collectLatest

/**
 * Stateful wrapper Composable that collects UI states from the ViewModel, resolves side effects,
 * and displays loading, error, or success layouts.
 */
@Composable
fun SduiScreenComposable(
    viewModel: SduiScreenViewModel,
    onNavigate: (route: String, params: Map<String, String>) -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    modifier: Modifier = Modifier,
    stateHolder: SduiStateHolder? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is SduiScreenUiEvent.NavigateTo -> onNavigate(event.route, event.params)
                is SduiScreenUiEvent.ShowSnackbar -> onShowSnackbar(event.message)
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is SduiScreenUiState.Loading -> {
                CircularProgressIndicator()
            }
            is SduiScreenUiState.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message)
                    Button(onClick = { viewModel.handleEvent(SduiScreenEvent.LoadScreen) }) {
                        Text("Retry")
                    }
                }
            }
            is SduiScreenUiState.Success -> {
                val effectiveStateHolder = stateHolder ?: remember(state.definition.screenId) {
                    SduiStateHolder()
                }
                SduiScreenContent(
                    definition = state.definition,
                    stateHolder = effectiveStateHolder,
                    onAction = { action ->
                        viewModel.handleEvent(SduiScreenEvent.OnAction(action))
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
