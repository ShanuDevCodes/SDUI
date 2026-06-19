package `in`.shanudevcodes.sdui.feature.screen.presentation

/**
 * One-off UI side-effects emitted from the ViewModel to be handled by the UI.
 */
sealed interface SduiScreenUiEvent {
    data class NavigateTo(val route: String, val params: Map<String, String>) : SduiScreenUiEvent
    data class ShowSnackbar(val message: String) : SduiScreenUiEvent
}
