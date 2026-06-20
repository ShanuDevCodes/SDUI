package `in`.shanudevcodes.sdui.feature.screen.domain.model

/**
 * Compile-time type-safe domain model representing a UI node.
 */
sealed interface SduiNode {
    val type: String
    val modifiers: List<SduiModifier>
    val children: List<SduiNode>

    data class ColumnNode(
        val space: Int,
        val verticalArrangement: String,
        val horizontalAlignment: String,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Column"
    }

    data class RowNode(
        val space: Int,
        val horizontalArrangement: String,
        val verticalAlignment: String,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Row"
    }

    data class BoxNode(
        val contentAlignment: String,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Box"
    }

    data class SpacerNode(
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Spacer"
        override val children: List<SduiNode> = emptyList()
    }

    data class TextNode(
        val text: String,
        val style: SduiStyle?,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Text"
        override val children: List<SduiNode> = emptyList()
    }

    data class ImageNode(
        val url: String,
        val contentDescription: String,
        val contentScale: String,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Image"
        override val children: List<SduiNode> = emptyList()
    }

    data class IconNode(
        val url: String,
        val name: String,
        val tint: String,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Icon"
        override val children: List<SduiNode> = emptyList()
    }

    data class DividerNode(
        val thickness: Int,
        val color: String,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Divider"
        override val children: List<SduiNode> = emptyList()
    }

    data class CardNode(
        val elevation: Int,
        val shape: String,
        val radius: Int,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Card"
    }

    data class SurfaceNode(
        val color: String,
        val elevation: Int,
        val shape: String,
        val radius: Int,
        val contentColor: String,
        val shadowElevation: Int,
        val borderWidth: Int,
        val borderColor: String,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Surface"
    }

    data class ButtonNode(
        val text: String,
        val enabled: Boolean,
        val onClick: SduiAction?,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Button"
    }

    data class TextFieldNode(
        val stateKey: String,
        val label: String,
        val placeholder: String,
        val enabled: Boolean,
        val singleLine: Boolean,
        val keyboardType: String,
        val visualTransformation: String,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "TextField"
        override val children: List<SduiNode> = emptyList()
    }

    data class SwitchNode(
        val stateKey: String,
        val enabled: Boolean,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Switch"
        override val children: List<SduiNode> = emptyList()
    }

    data class CheckboxNode(
        val stateKey: String,
        val enabled: Boolean,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Checkbox"
        override val children: List<SduiNode> = emptyList()
    }

    data class RadioButtonNode(
        val stateKey: String,
        val valueStr: String,
        val enabled: Boolean,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "RadioButton"
        override val children: List<SduiNode> = emptyList()
    }

    data class SliderNode(
        val stateKey: String,
        val valueRangeMin: Float,
        val valueRangeMax: Float,
        val steps: Int,
        val enabled: Boolean,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Slider"
        override val children: List<SduiNode> = emptyList()
    }

    data class DropdownMenuNode(
        val stateKey: String,
        val label: String,
        val options: List<String>,
        val enabled: Boolean,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "DropdownMenu"
        override val children: List<SduiNode> = emptyList()
    }

    data class LazyColumnNode(
        val space: Int,
        val verticalArrangement: String,
        val horizontalAlignment: String,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "LazyColumn"
    }

    data class LazyRowNode(
        val space: Int,
        val horizontalArrangement: String,
        val verticalAlignment: String,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "LazyRow"
    }

    data class LazyGridNode(
        val columns: Int,
        val space: Int,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "LazyGrid"
    }

    data class ConditionalNode(
        val stateKey: String,
        val operator: String,
        val compareValueStr: String?,
        val thenComponent: SduiNode?,
        val elseComponent: SduiNode?,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Conditional"
    }

    data class VisibleNode(
        val stateKey: String,
        val operator: String,
        val compareValueStr: String?,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Visible"
    }

    data class FallbackNode(
        val originalType: String,
        val originalProps: Map<String, kotlinx.serialization.json.JsonElement> = emptyMap(),
        val originalActions: Map<String, SduiAction> = emptyMap(),
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String get() = originalType   // preserves custom type for registry lookup
    }

    data class ScaffoldNode(
        val topBarTitle: String,
        val topBarNavigationIcon: String,
        val showTopBar: Boolean,
        override val children: List<SduiNode>,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "Scaffold"
    }

    data class IconButtonNode(
        val icon: String,
        val contentDescription: String,
        val enabled: Boolean,
        val onClick: SduiAction?,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "IconButton"
        override val children: List<SduiNode> = emptyList()
    }

    data class CircularProgressNode(
        val progress: Float,
        val color: String,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "CircularProgress"
        override val children: List<SduiNode> = emptyList()
    }

    data class LinearProgressNode(
        val progress: Float,
        val color: String,
        val trackColor: String,
        override val modifiers: List<SduiModifier>
    ) : SduiNode {
        override val type: String = "LinearProgress"
        override val children: List<SduiNode> = emptyList()
    }
}
