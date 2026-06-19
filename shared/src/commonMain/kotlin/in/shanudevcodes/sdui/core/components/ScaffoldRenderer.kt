package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val topBarTitle = component.stringProp("topBarTitle")
    val topBarNavigationIcon = component.stringProp("topBarNavigationIcon")
    val showTopBar = component.booleanProp("showTopBar", false)
    val onAction = LocalSduiActionHandler.current

    Scaffold(
        modifier = modifier,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(topBarTitle) },
                    navigationIcon = {
                        if (topBarNavigationIcon.isNotEmpty()) {
                            IconButton(onClick = {
                                onAction(SduiActionDto(type = "Navigate", route = "back"))
                            }) {
                                Icon(
                                    imageVector = resolveScaffoldIcon(topBarNavigationIcon),
                                    contentDescription = topBarNavigationIcon
                                )
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

private fun resolveScaffoldIcon(name: String) = when (name.lowercase()) {
    "arrowback", "arrow_back", "back" -> androidx.compose.material.icons.Icons.Default.ArrowBack
    "menu" -> androidx.compose.material.icons.Icons.Default.Menu
    "close" -> androidx.compose.material.icons.Icons.Default.Close
    else -> androidx.compose.material.icons.Icons.Default.ArrowBack
}
