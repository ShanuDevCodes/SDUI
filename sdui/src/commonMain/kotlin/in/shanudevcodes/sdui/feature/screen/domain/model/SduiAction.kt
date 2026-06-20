package `in`.shanudevcodes.sdui.feature.screen.domain.model

/**
 * Domain representation of actions independent of serialization and network layers.
 */
public data class SduiAction(
    public val type: String,
    public val route: String? = null,
    public val params: Map<String, String> = emptyMap(),
    public val url: String? = null,
    public val stateKey: String? = null,
    public val valueStr: String? = null,
    public val endpoint: String? = null,
    public val method: String? = null,
    public val bodyStr: String? = null,
    public val onSuccess: SduiAction? = null,
    public val onError: SduiAction? = null,
    public val message: String? = null,
    public val actionLabel: String? = null,
    public val onAction: SduiAction? = null,
    public val title: String? = null,
    public val confirmText: String? = null,
    public val dismissText: String? = null,
    public val onConfirm: SduiAction? = null,
    public val onDismiss: SduiAction? = null,
    public val name: String? = null,
    public val payload: Map<String, String> = emptyMap(),
    public val actions: List<SduiAction> = emptyList(),
    public val eventName: String? = null,
    public val operator: String? = null,
    public val compareValueStr: String? = null,
    public val thenAction: SduiAction? = null,
    public val elseAction: SduiAction? = null
)
