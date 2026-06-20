package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Props: topBarTitle, topBarNavigationIconUrl (CDN URL), topBarNavigationIcon (name/key), showTopBar (bool).
 * The navigation icon button uses topBarNavigationIcon (or "back" by default) as its contentDescription
 * and dispatches Navigate(route=value) when tapped.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScaffoldRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val topBarTitle = component.stringProp("topBarTitle")
    val navIconUrl = component.stringProp("topBarNavigationIconUrl")
    // topBarNavigationIcon can be a name/key like "back"; used as contentDescription & route
    val navIconKey = component.stringProp("topBarNavigationIcon")
    val showTopBar = component.booleanProp("showTopBar", false)
    val onAction = LocalSduiActionHandler.current

    // Show nav icon if either a URL or a key is provided
    val hasNavIcon = navIconUrl.isNotEmpty() || navIconKey.isNotEmpty()
    val navIconDesc = navIconKey.ifEmpty { "back" }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(topBarTitle) },
                    navigationIcon = {
                        if (hasNavIcon) {
                            IconButton(
                                onClick = {
                                    onAction(SduiActionDto(type = "Navigate", route = navIconDesc))
                                }
                            ) {
                                if (navIconUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = navIconUrl,
                                        contentDescription = navIconDesc,
                                        modifier = Modifier.padding(4.dp),
                                        contentScale = ContentScale.Fit,
                                        colorFilter = ColorFilter.tint(Color.White)
                                    )
                                } else {
                                    // No image URL — render an accessible placeholder
                                    androidx.compose.foundation.layout.Box(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .semantics { contentDescription = navIconDesc }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            component.children.forEach { child ->
                SduiRenderer(component = child, stateHolder = stateHolder)
            }
        }
    }
}
