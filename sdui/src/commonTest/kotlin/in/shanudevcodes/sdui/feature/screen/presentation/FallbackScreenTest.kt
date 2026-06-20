package `in`.shanudevcodes.sdui.feature.screen.presentation

import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class FallbackScreenTest {

    @Test
    fun testFallbackScreen_rendersMessageAndTriggersRetry() = runComposeUiTest {
        var retryClicked = false
        setContent {
            FallbackScreen(
                reason = "Incompatible schema version 2.0.0",
                onRetry = { retryClicked = true },
                modifier = Modifier
            )
        }

        onNodeWithText("Unable to load screen").assertExists()
        onNodeWithText("Incompatible schema version 2.0.0").assertExists()
        onNodeWithText("Retry").performClick()
        assertTrue(retryClicked)
    }
}
