package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Renderer for the LazyRow component. Renders dynamic children inside a lazy horizontal list.
 */
@Composable
internal fun LazyRowRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val space = component.intProp("space", -1)
    val horizontalArrangement = if (space >= 0) {
        Arrangement.spacedBy(space.dp)
    } else {
        when (component.stringProp("horizontalArrangement")) {
            "Center", "CenterHorizontally" -> Arrangement.Center
            "End" -> Arrangement.End
            "SpaceBetween" -> Arrangement.SpaceBetween
            "SpaceAround" -> Arrangement.SpaceAround
            "SpaceEvenly" -> Arrangement.SpaceEvenly
            else -> Arrangement.Start
        }
    }

    val verticalAlignment = when (component.stringProp("verticalAlignment")) {
        "Center", "CenterVertically" -> Alignment.CenterVertically
        "Bottom" -> Alignment.Bottom
        else -> Alignment.Top
    }

    LazyRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {
        items(component.children) { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }
}
