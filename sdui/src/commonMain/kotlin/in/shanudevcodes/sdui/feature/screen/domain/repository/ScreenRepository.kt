package `in`.shanudevcodes.sdui.feature.screen.domain.repository

import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition

/**
 * Domain repository interface for fetching SDUI screen configurations.
 */
public interface ScreenRepository {
    public suspend fun getScreen(
        screenId: String,
        params: Map<String, String> = emptyMap()
    ): Result<ScreenDefinition>
}
