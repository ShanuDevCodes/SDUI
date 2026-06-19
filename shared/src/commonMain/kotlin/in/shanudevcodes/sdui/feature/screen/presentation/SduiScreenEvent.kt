package `in`.shanudevcodes.sdui.feature.screen.presentation

import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiAction

/**
 * Events sent from the presentation layer (Composables) to the SduiScreenViewModel.
 */
sealed interface SduiScreenEvent {
    object LoadScreen : SduiScreenEvent
    data class OnAction(val action: SduiAction) : SduiScreenEvent
    data class OnStateChange(val key: String, val value: String) : SduiScreenEvent
}
