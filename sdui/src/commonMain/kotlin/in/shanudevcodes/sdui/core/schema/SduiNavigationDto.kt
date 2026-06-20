package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.Serializable

/**
 * DTO representing navigation instructions (push, replace, popToRoot, deepLink).
 */
@Serializable
internal data class SduiNavigationDto(
    val type: String,
    val route: String? = null,
    val params: Map<String, String> = emptyMap(),
    val url: String? = null
)
