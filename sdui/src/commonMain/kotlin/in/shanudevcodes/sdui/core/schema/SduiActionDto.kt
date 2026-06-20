package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * DTO representing an action triggered by user interaction or lifecycle events.
 */
@Serializable
public data class SduiActionDto(
    val type: String,
    val route: String? = null,
    val params: Map<String, String> = emptyMap(),
    val url: String? = null,
    val stateKey: String? = null,
    val value: JsonElement? = null,
    val endpoint: String? = null,
    val method: String? = null,
    val body: JsonElement? = null,
    val onSuccess: SduiActionDto? = null,
    val onError: SduiActionDto? = null,
    val message: String? = null,
    val actionLabel: String? = null,
    val onAction: SduiActionDto? = null,
    val title: String? = null,
    val confirmText: String? = null,
    val dismissText: String? = null,
    val onConfirm: SduiActionDto? = null,
    val onDismiss: SduiActionDto? = null,
    val name: String? = null,
    val payload: Map<String, String> = emptyMap(),
    val actions: List<SduiActionDto> = emptyList(),
    val eventName: String? = null,
    val operator: String? = null,
    val compareValue: JsonElement? = null,
    val thenAction: SduiActionDto? = null,
    val elseAction: SduiActionDto? = null
)
