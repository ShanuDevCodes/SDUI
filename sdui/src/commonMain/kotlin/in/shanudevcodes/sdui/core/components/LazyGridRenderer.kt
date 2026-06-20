package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

/**
 * Renderer for the LazyGrid component. Renders children in a lazy vertical grid.
 */
@Composable
internal fun LazyGridRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val columns = component.intProp("columns", 2)
    val space = component.intProp("space", 0)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        verticalArrangement = Arrangement.spacedBy(space.dp),
        horizontalArrangement = Arrangement.spacedBy(space.dp),
        modifier = modifier
    ) {
        items(component.children) { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }
}
