package `in`.shanudevcodes.sdui.feature.screen

import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode
import `in`.shanudevcodes.sdui.feature.screen.presentation.SduiScreenUiState
import `in`.shanudevcodes.sdui.feature.screen.presentation.SduiScreenViewModel
import `in`.shanudevcodes.sdui.testing.FakeScreenRepository
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests verifying state updates and repository calls inside the SduiScreenViewModel.
 */
class SduiScreenViewModelTest {

    @Test
    fun testViewModel_loadsSuccessState() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val definition = ScreenDefinition(
            screenId = "home",
            schemaVersion = "1.0",
            title = "Home",
            root = SduiNode.SpacerNode(emptyList())
        )
        val repository = FakeScreenRepository().apply {
            registerScreen("home", definition)
        }
        val viewModel = SduiScreenViewModel("home", repository, testDispatcher)

        assertTrue(viewModel.uiState.value is SduiScreenUiState.Success)
        val successState = viewModel.uiState.value as SduiScreenUiState.Success
        assertEquals("Home", successState.definition.title)
    }

    @Test
    fun testViewModel_loadsErrorState() = runTest {
        val testDispatcher = UnconfinedTestDispatcher()
        val repository = FakeScreenRepository().apply {
            setError(Exception("Network error"))
        }
        val viewModel = SduiScreenViewModel("home", repository, testDispatcher)

        assertTrue(viewModel.uiState.value is SduiScreenUiState.Error)
        val errorState = viewModel.uiState.value as SduiScreenUiState.Error
        assertEquals("Network error", errorState.message)
    }
}
