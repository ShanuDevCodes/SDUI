package `in`.shanudevcodes.sdui.core.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.navigation3.runtime.NavBackStack
import `in`.shanudevcodes.sdui.core.registry.ComponentRegistry
import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode
import `in`.shanudevcodes.sdui.feature.screen.domain.repository.ScreenRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SduiNavDisplayTest {

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
    fun testNavDisplay_rendersInitialScreen() = runComposeUiTest {
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
                        text = "Navigated Welcome Screen",
                        style = null,
                        modifiers = emptyList()
                    )
                ),
                modifiers = emptyList()
            )
        )

        val repository = FakeScreenRepository(Result.success(definition))

        setContent {
            // Instantiate backstack directly without saved state serialization configuration for tests
            val backStack = androidx.compose.runtime.remember {
                NavBackStack<SduiRoute>(SduiRoute.Screen("welcome"))
            }
            SduiNavDisplay(
                backStack = backStack,
                repository = repository,
                onNavigate = { _, _ -> },
                onShowSnackbar = {}
            )
        }

        onNodeWithText("Navigated Welcome Screen").assertExists()
    }
}
