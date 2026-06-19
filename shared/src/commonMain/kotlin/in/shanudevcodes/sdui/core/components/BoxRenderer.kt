package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun BoxRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val contentAlignment = when (component.stringProp("contentAlignment")) {
        "TopStart" -> Alignment.TopStart
        "TopCenter" -> Alignment.TopCenter
        "TopEnd" -> Alignment.TopEnd
        "CenterStart" -> Alignment.CenterStart
        "Center" -> Alignment.Center
        "CenterEnd" -> Alignment.CenterEnd
        "BottomStart" -> Alignment.BottomStart
        "BottomCenter" -> Alignment.BottomCenter
        "BottomEnd" -> Alignment.BottomEnd
        else -> Alignment.TopStart
    }

    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        component.children.forEach { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }
}
