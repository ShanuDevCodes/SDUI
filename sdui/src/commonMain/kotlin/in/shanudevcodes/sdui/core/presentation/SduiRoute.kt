package `in`.shanudevcodes.sdui.core.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Sealed interface representing type-safe routes for the SDUI Navigation 3 system.
 */
@Serializable
public sealed interface SduiRoute : NavKey {
    @Serializable
    public data class Screen(
        val screenId: String,
        val params: Map<String, String> = emptyMap()
    ) : SduiRoute
}
