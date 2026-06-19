package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.action
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun IconButtonRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val iconName = component.stringProp("icon")
    val contentDescription = component.stringProp("contentDescription", iconName)
    val enabled = component.booleanProp("enabled", true)
    val onClickAction = component.action("onClick")
    val onAction = LocalSduiActionHandler.current

    IconButton(
        onClick = { onClickAction?.let { onAction(it) } },
        modifier = modifier,
        enabled = enabled
    ) {
        Icon(
            imageVector = resolveIconButtonIcon(iconName),
            contentDescription = contentDescription
        )
    }
}

private fun resolveIconButtonIcon(name: String) = when (name.lowercase()) {
    "home" -> androidx.compose.material.icons.Icons.Default.Home
    "search" -> androidx.compose.material.icons.Icons.Default.Search
    "settings" -> androidx.compose.material.icons.Icons.Default.Settings
    "menu" -> androidx.compose.material.icons.Icons.Default.Menu
    "arrowback", "arrow_back", "back" -> androidx.compose.material.icons.Icons.Default.ArrowBack
    "add" -> androidx.compose.material.icons.Icons.Default.Add
    "check" -> androidx.compose.material.icons.Icons.Default.Check
    "close" -> androidx.compose.material.icons.Icons.Default.Close
    "edit" -> androidx.compose.material.icons.Icons.Default.Edit
    "person" -> androidx.compose.material.icons.Icons.Default.Person
    "delete" -> androidx.compose.material.icons.Icons.Default.Delete
    "favorite" -> androidx.compose.material.icons.Icons.Default.Favorite
    "share" -> androidx.compose.material.icons.Icons.Default.Share
    "warning" -> androidx.compose.material.icons.Icons.Default.Warning
    else -> androidx.compose.material.icons.Icons.Default.Info
}
