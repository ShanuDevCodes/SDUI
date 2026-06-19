package `in`.shanudevcodes.sdui.feature.screen.presentation

import `in`.shanudevcodes.sdui.core.schema.SduiActionDto

/**
 * Holds state for an SDUI-driven alert dialog.
 */
data class SduiDialogState(
    val isVisible: Boolean = false,
    val title: String = "",
    val message: String = "",
    val confirmText: String? = null,
    val dismissText: String? = null,
    val onConfirm: SduiActionDto? = null,
    val onDismiss: SduiActionDto? = null
)
