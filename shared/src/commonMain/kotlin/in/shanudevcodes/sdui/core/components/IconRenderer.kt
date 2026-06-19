package `in`.shanudevcodes.sdui.core.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.resolvedStringProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

private val ICON_MAP = mapOf(
    // Navigation
    "home" to Icons.Default.Home,
    "search" to Icons.Default.Search,
    "settings" to Icons.Default.Settings,
    "menu" to Icons.Default.Menu,
    "arrowback" to Icons.Default.ArrowBack,
    "arrowforward" to Icons.Default.ArrowForward,
    "arrowdropdown" to Icons.Default.ArrowDropDown,
    "arrowdropup" to Icons.Default.ArrowDropUp,
    "morevert" to Icons.Default.MoreVert,
    "morehoriz" to Icons.Default.MoreHoriz,
    "close" to Icons.Default.Close,
    "clear" to Icons.Default.Clear,
    // Actions
    "add" to Icons.Default.Add,
    "edit" to Icons.Default.Edit,
    "delete" to Icons.Default.Delete,
    "share" to Icons.Default.Share,
    "send" to Icons.Default.Send,
    "save" to Icons.Default.Save,
    "star" to Icons.Default.Star,
    "starborder" to Icons.Default.StarBorder,
    "bookmark" to Icons.Default.Bookmark,
    "bookmarkborder" to Icons.Default.BookmarkBorder,
    "favorite" to Icons.Default.Favorite,
    "favoriteborder" to Icons.Default.FavoriteBorder,
    "thumbup" to Icons.Default.ThumbUp,
    "thumbdown" to Icons.Default.ThumbDown,
    "refresh" to Icons.Default.Refresh,
    "filterlist" to Icons.Default.FilterList,
    "sort" to Icons.Default.Sort,
    // Content
    "create" to Icons.Default.Create,
    "contentcopy" to Icons.Default.ContentCopy,
    "contentpaste" to Icons.Default.ContentPaste,
    "contentcut" to Icons.Default.ContentCut,
    "undo" to Icons.Default.Undo,
    "redo" to Icons.Default.Redo,
    "selectall" to Icons.Default.SelectAll,
    "link" to Icons.Default.Link,
    // Communication
    "call" to Icons.Default.Call,
    "email" to Icons.Default.Email,
    "message" to Icons.Default.Message,
    "notifications" to Icons.Default.Notifications,
    "notificationsnone" to Icons.Default.NotificationsNone,
    "person" to Icons.Default.Person,
    "personadd" to Icons.Default.PersonAdd,
    "group" to Icons.Default.Group,
    // Media
    "playarrow" to Icons.Default.PlayArrow,
    "pause" to Icons.Default.Pause,
    "stop" to Icons.Default.Stop,
    "skipnext" to Icons.Default.SkipNext,
    "skipprevious" to Icons.Default.SkipPrevious,
    "volumeup" to Icons.Default.VolumeUp,
    "volumedown" to Icons.Default.VolumeDown,
    "volumemute" to Icons.Default.VolumeMute,
    // Status
    "check" to Icons.Default.Check,
    "checkcircle" to Icons.Default.CheckCircle,
    "cancel" to Icons.Default.Cancel,
    "error" to Icons.Default.Error,
    "warning" to Icons.Default.Warning,
    "info" to Icons.Default.Info,
    "help" to Icons.Default.Help,
    "helpoutline" to Icons.Default.HelpOutline,
    "lock" to Icons.Default.Lock,
    "lockopen" to Icons.Default.LockOpen,
    "visibility" to Icons.Default.Visibility,
    "visibilityoff" to Icons.Default.VisibilityOff,
    // Files
    "folder" to Icons.Default.Folder,
    "folderopen" to Icons.Default.FolderOpen,
    "attachfile" to Icons.Default.AttachFile,
    // Maps
    "locationon" to Icons.Default.LocationOn,
    "locationoff" to Icons.Default.LocationOff,
    "mylocation" to Icons.Default.MyLocation,
    "map" to Icons.Default.Map,
    // Commerce
    "shoppingcart" to Icons.Default.ShoppingCart,
    "creditcard" to Icons.Default.CreditCard,
    "payment" to Icons.Default.Payment,
    // Misc
    "daterange" to Icons.Default.DateRange,
    "schedule" to Icons.Default.Schedule,
    "accesstime" to Icons.Default.AccessTime,
    "language" to Icons.Default.Language,
    "public" to Icons.Default.Public,
    "palette" to Icons.Default.Palette,
    "brush" to Icons.Default.Brush,
    "image" to Icons.Default.Image,
    "photo" to Icons.Default.Photo,
    "camera" to Icons.Default.Camera,
)

@Composable
fun IconRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val state by stateHolder.state.collectAsState()
    val name = component.resolvedStringProp("name", state)
    val tintStr = component.stringProp("tint")

    val key = name.lowercase().replace("_", "")
    val icon = ICON_MAP[key] ?: Icons.Default.Info

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
