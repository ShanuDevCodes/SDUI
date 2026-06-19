package `in`.shanudevcodes.sdui.feature.screen

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
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
}
