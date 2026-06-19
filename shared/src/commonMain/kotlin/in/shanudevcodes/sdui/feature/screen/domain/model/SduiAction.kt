package `in`.shanudevcodes.sdui.feature.screen.domain.model

/**
 * Domain representation of actions independent of serialization and network layers.
 */
data class SduiAction(
    val type: String,
    val route: String? = null,
    val params: Map<String, String> = emptyMap(),
    val url: String? = null,
    val stateKey: String? = null,
    val valueStr: String? = null,
    val endpoint: String? = null,
    val method: String? = null,
    val bodyStr: String? = null,
    val onSuccess: SduiAction? = null,
    val onError: SduiAction? = null,
    val message: String? = null,
    val actionLabel: String? = null,
    val onAction: SduiAction? = null,
    val title: String? = null,
    val confirmText: String? = null,
    val dismissText: String? = null,
    val onConfirm: SduiAction? = null,
    val onDismiss: SduiAction? = null,
    val name: String? = null,
    val payload: Map<String, String> = emptyMap(),
    val actions: List<SduiAction> = emptyList(),
    val eventName: String? = null
)
