package `in`.shanudevcodes.sdui.core.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Sealed interface representing type-safe routes for the SDUI Navigation 3 system.
 */
@Serializable
sealed interface SduiRoute : NavKey {

    /**
     * Route representing a dynamic server-driven UI screen destination.
     *
     * @property screenId The unique identifier of the screen payload to fetch.
     * @property params Optional query parameters or routing parameters to pass to the API request.
     */
    @Serializable
    data class Screen(
        val screenId: String,
        val params: Map<String, String> = emptyMap()
    ) : SduiRoute
}
