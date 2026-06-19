package `in`.shanudevcodes.sdui.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonPrimitive

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

    val scrollable = component.booleanProp("scrollable", false)
    val scrollState = if (scrollable) rememberScrollState() else null
    val columnModifier = if (scrollState != null) modifier.verticalScroll(scrollState) else modifier

    Column(
        modifier = columnModifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        component.children.forEach { child ->
            val weightValue = child.modifiers
                .firstOrNull { it.type == "weight" }
                ?.value?.jsonPrimitive?.floatOrNull
            val alignValue = child.modifiers
                .firstOrNull { it.type == "align" }
                ?.value?.jsonPrimitive?.content
            val alignModifier: Modifier = if (alignValue != null) {
                val alignment = when (alignValue) {
                    "Start" -> Alignment.Start
                    "CenterHorizontally" -> Alignment.CenterHorizontally
                    "End" -> Alignment.End
                    else -> null
                }
                if (alignment != null) Modifier.align(alignment) else Modifier
            } else Modifier
            val scopeModifier = if (weightValue != null) Modifier.weight(weightValue).then(alignModifier) else alignModifier
            SduiRenderer(component = child, stateHolder = stateHolder, modifier = scopeModifier)
        }
    }
}
