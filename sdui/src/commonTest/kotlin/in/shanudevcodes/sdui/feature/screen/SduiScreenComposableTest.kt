package `in`.shanudevcodes.sdui.feature.screen

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode
import `in`.shanudevcodes.sdui.feature.screen.domain.repository.ScreenRepository
import `in`.shanudevcodes.sdui.feature.screen.presentation.SduiScreenComposable
import `in`.shanudevcodes.sdui.feature.screen.presentation.SduiScreenViewModel
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Integration Compose UI tests for the SduiScreenComposable and overall rendering pipeline.
 */
@OptIn(ExperimentalTestApi::class)
class SduiScreenComposableTest {

    private class FakeScreenRepository(
        private val result: Result<ScreenDefinition>
    ) : ScreenRepository {
        override suspend fun getScreen(
            screenId: String,
            params: Map<String, String>
        ): Result<ScreenDefinition> = result
    }

    @BeforeTest
    fun setUp() {
        ComponentRegistry.reset()
    }

    @AfterTest
    fun tearDown() {
        ComponentRegistry.reset()
    }

    @Test
    fun testScreenComposable_rendersSuccessContent() = runComposeUiTest {
        val definition = ScreenDefinition(
            screenId = "welcome",
            schemaVersion = "1.0",
            title = "Welcome Screen",
            root = SduiNode.ColumnNode(
                space = -1,
                verticalArrangement = "",
                horizontalAlignment = "",
                children = listOf(
                    SduiNode.TextNode(
                        text = "Welcome to SDUI",
                        style = null,
                        modifiers = emptyList()
                    )
                ),
                modifiers = emptyList()
            )
        )

        val repository = FakeScreenRepository(Result.success(definition))
        val testDispatcher = UnconfinedTestDispatcher()
        val viewModel = SduiScreenViewModel("welcome", repository, testDispatcher)

        setContent {
            SduiScreenComposable(
                viewModel = viewModel,
                onNavigate = { _, _ -> },
                onShowSnackbar = {}
            )
        }

        onNodeWithText("Welcome to SDUI").assertExists()
    }

    @Test
    fun testScreenComposable_rendersFallbackScreenOnError() = runComposeUiTest {
        val repository = FakeScreenRepository(Result.failure(Exception("Could not load payload")))
        val testDispatcher = UnconfinedTestDispatcher()
        val viewModel = SduiScreenViewModel("error_screen", repository, testDispatcher)

        setContent {
            SduiScreenComposable(
                viewModel = viewModel,
                onNavigate = { _, _ -> },
                onShowSnackbar = {}
            )
        }

        onNodeWithText("Unable to load screen").assertExists()
        onNodeWithText("Could not load payload").assertExists()
        onNodeWithText("Retry").assertExists()
    }

    @Test
    fun testScreenComposable_rendersAlertDialog_onShowDialog() = runComposeUiTest {
        val dialogNode = SduiNode.ButtonNode(
            text = "Show Alert",
            enabled = true,
            onClick = `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiAction(
                type = "ShowDialog",
                title = "Modal Title",
                message = "Modal Body",
                confirmText = "Proceed",
                dismissText = "Cancel"
            ),
            children = emptyList(),
            modifiers = emptyList()
        )
        val definition = ScreenDefinition(
            screenId = "dialog_screen",
            schemaVersion = "1.0",
            title = "Dialog Title",
            root = dialogNode
        )
        val repository = FakeScreenRepository(Result.success(definition))
        val testDispatcher = UnconfinedTestDispatcher()
        val viewModel = SduiScreenViewModel("dialog_screen", repository, testDispatcher)

        setContent {
            SduiScreenComposable(
                viewModel = viewModel,
                onNavigate = { _, _ -> },
                onShowSnackbar = {}
            )
        }

        onNodeWithText("Show Alert").performClick()
        onNodeWithText("Modal Title").assertExists()
        onNodeWithText("Modal Body").assertExists()
        onNodeWithText("Proceed").assertExists()
        onNodeWithText("Cancel").performClick()
    }
}

