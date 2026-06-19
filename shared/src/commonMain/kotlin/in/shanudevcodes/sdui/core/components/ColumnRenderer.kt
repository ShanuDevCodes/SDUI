package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun ColumnRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val space = component.intProp("space", -1)
    val verticalArrangement = if (space >= 0) {
        Arrangement.spacedBy(space.dp)
    } else {
        when (component.stringProp("verticalArrangement")) {
            "Center", "CenterVertically" -> Arrangement.Center
            "Bottom" -> Arrangement.Bottom
            "SpaceBetween" -> Arrangement.SpaceBetween
            "SpaceAround" -> Arrangement.SpaceAround
            "SpaceEvenly" -> Arrangement.SpaceEvenly
            else -> Arrangement.Top
        }
    }

    val horizontalAlignment = when (component.stringProp("horizontalAlignment")) {
        "Center", "CenterHorizontally" -> Alignment.CenterHorizontally
        "End" -> Alignment.End
        else -> Alignment.Start
    }

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        component.children.forEach { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }
}
