package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder

@Composable
fun CardRenderer(
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) {
    val elevation = component.intProp("elevation", 1)
    val shapeName = component.stringProp("shape")
    val radius = component.intProp("radius", 8)

    val shape: Shape = when (shapeName) {
        "Circle" -> CircleShape
        "Rectangle" -> RectangleShape
        "RoundedCorner", "Rounded" -> RoundedCornerShape(radius.dp)
        else -> RoundedCornerShape(radius.dp)
    }

    Card(
        modifier = modifier,
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp)
    ) {
        component.children.forEach { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }
}
