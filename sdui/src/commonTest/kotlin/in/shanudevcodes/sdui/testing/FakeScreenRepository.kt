package `in`.shanudevcodes.sdui.testing

import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition
import `in`.shanudevcodes.sdui.feature.screen.domain.repository.ScreenRepository

/**
 * Reusable test double for ScreenRepository.
 * Supports registering screen definitions and simulating failures.
 */
class FakeScreenRepository : ScreenRepository {
    private val screens = mutableMapOf<String, ScreenDefinition>()
    private var error: Throwable? = null

    fun registerScreen(screenId: String, definition: ScreenDefinition) {
        screens[screenId] = definition
    }

    fun setError(throwable: Throwable?) {
        error = throwable
    }

    override suspend fun getScreen(screenId: String, params: Map<String, String>): Result<ScreenDefinition> {
        val currentError = error
        if (currentError != null) {
            return Result.failure(currentError)
        }
        val screen = screens[screenId] ?: return Result.failure(IllegalArgumentException("Screen not found: $screenId"))
        return Result.success(screen)
    }
}
