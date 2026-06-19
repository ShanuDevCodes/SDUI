package `in`.shanudevcodes.sdui.feature.screen.presentation

import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition

/**
 * UI State representing the current fetch status of the SDUI screen.
 */
sealed interface SduiScreenUiState {
    object Loading : SduiScreenUiState
    data class Success(val definition: ScreenDefinition) : SduiScreenUiState
    data class Error(val message: String) : SduiScreenUiState
}
