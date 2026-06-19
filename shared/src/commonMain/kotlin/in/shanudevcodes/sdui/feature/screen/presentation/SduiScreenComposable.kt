package `in`.shanudevcodes.sdui.feature.screen.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.action.ActionDispatcher
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import `in`.shanudevcodes.sdui.feature.screen.data.mapper.ScreenMapper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Stateful wrapper Composable that collects UI states from the ViewModel, resolves side effects,
 * and displays loading, error, or success layouts.
 */
@Composable
fun SduiScreenComposable(
    viewModel: SduiScreenViewModel,
    onNavigate: (route: String, params: Map<String, String>) -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    modifier: Modifier = Modifier
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
                FallbackScreen(
                    reason = state.message,
                    onRetry = { viewModel.handleEvent(SduiScreenEvent.LoadScreen) }
                )
            }
            is SduiScreenUiState.Success -> {
                val stateHolder = remember(state.definition.screenId) {
                    SduiStateHolder(state.definition.initialState)
                }
                var dialogState by remember { mutableStateOf(SduiDialogState()) }
                val scope = rememberCoroutineScope()

                val dispatcher = remember(stateHolder) {
                    ActionDispatcher(
                        stateHolder = stateHolder,
                        onNavigate = { route, params -> onNavigate(route, params) },
                        onGoBack = { onNavigate("back", emptyMap()) },
                        onReplace = { route, params -> onNavigate(route, params) },
                        onShowSnackbar = { message, _, _ -> onShowSnackbar(message) },
                        onShowDialog = { title, message, confirmText, dismissText, onConfirm, onDismiss ->
                            dialogState = SduiDialogState(
                                isVisible = true,
                                title = title,
                                message = message,
                                confirmText = confirmText,
                                dismissText = dismissText,
                                onConfirm = onConfirm,
                                onDismiss = onDismiss
                            )
                        }
                    )
                }

                if (dialogState.isVisible) {
                    AlertDialog(
                        onDismissRequest = {
                            val dismissAction = dialogState.onDismiss
                            dialogState = dialogState.copy(isVisible = false)
                            if (dismissAction != null) {
                                scope.launch { dispatcher.dispatch(dismissAction) }
                            }
                        },
                        title = { Text(dialogState.title) },
                        text = { Text(dialogState.message) },
                        confirmButton = {
                            TextButton(onClick = {
                                val confirmAction = dialogState.onConfirm
                                dialogState = dialogState.copy(isVisible = false)
                                if (confirmAction != null) {
                                    scope.launch { dispatcher.dispatch(confirmAction) }
                                }
                            }) {
                                Text(dialogState.confirmText ?: "OK")
                            }
                        },
                        dismissButton = if (dialogState.dismissText != null) {
                            {
                                TextButton(onClick = {
                                    val dismissAction = dialogState.onDismiss
                                    dialogState = dialogState.copy(isVisible = false)
                                    if (dismissAction != null) {
                                        scope.launch { dispatcher.dispatch(dismissAction) }
                                    }
                                }) {
                                    Text(dialogState.dismissText!!)
                                }
                            }
                        } else null
                    )
                }

                SduiScreenContent(
                    definition = state.definition,
                    stateHolder = stateHolder,
                    onAction = { action ->
                        scope.launch { dispatcher.dispatch(ScreenMapper.mapActionToDto(action)) }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
