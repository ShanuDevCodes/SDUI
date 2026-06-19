package `in`.shanudevcodes.sdui.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun ImageRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val url = component.stringProp("url")
    val contentDescription = component.stringProp("contentDescription")
    val contentScale = when (component.stringProp("contentScale")) {
        "Crop", "CenterCrop" -> ContentScale.Crop
        "Fit", "FitCenter" -> ContentScale.Fit
        "Fill", "FillBounds" -> ContentScale.FillBounds
        "Inside" -> ContentScale.Inside
        "None" -> ContentScale.None
        else -> ContentScale.Fit
    }

    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}
