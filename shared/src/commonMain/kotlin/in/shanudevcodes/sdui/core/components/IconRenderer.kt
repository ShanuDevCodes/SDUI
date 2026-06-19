package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun IconRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val name = component.stringProp("name")
    val tintStr = component.stringProp("tint")

    val icon = when (name.lowercase()) {
        "home" -> Icons.Default.Home
        "search" -> Icons.Default.Search
        "settings" -> Icons.Default.Settings
        "menu" -> Icons.Default.Menu
        "arrowback", "arrow_back", "back" -> Icons.Default.ArrowBack
        "add" -> Icons.Default.Add
        "check" -> Icons.Default.Check
        "close" -> Icons.Default.Close
        "info" -> Icons.Default.Info
        "edit" -> Icons.Default.Edit
        "warning" -> Icons.Default.Warning
        "favorite" -> Icons.Default.Favorite
        "share" -> Icons.Default.Share
        "delete" -> Icons.Default.Delete
        "person" -> Icons.Default.Person
        else -> Icons.Default.Info
    }

    val tint = parseHexColor(tintStr)

    Icon(
        imageVector = icon,
        contentDescription = name,
        modifier = modifier,
        tint = if (tint != Color.Unspecified) tint else androidx.compose.material3.LocalContentColor.current
    )
}

private fun parseHexColor(hexColorString: String?): Color {
    if (hexColorString.isNullOrEmpty()) return Color.Unspecified
    val cleanHex = hexColorString.removePrefix("#")
    return try {
        when (cleanHex.length) {
            6 -> Color(cleanHex.toLong(16) or 0xFF000000)
            8 -> Color(cleanHex.toLong(16))
            else -> Color.Unspecified
        }
    } catch (e: Exception) {
        Color.Unspecified
    }
}
