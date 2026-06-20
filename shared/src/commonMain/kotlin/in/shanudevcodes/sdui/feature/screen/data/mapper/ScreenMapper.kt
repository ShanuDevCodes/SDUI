package `in`.shanudevcodes.sdui.feature.screen.data.mapper

import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.SduiModifierDto
import `in`.shanudevcodes.sdui.core.schema.SduiScreenDto
import `in`.shanudevcodes.sdui.core.schema.SduiStyleDto
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp
import `in`.shanudevcodes.sdui.core.schema.styleProp
import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiAction
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiModifier
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiStyle
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Data-layer mapper converting network serialization DTOs to pure Kotlin domain models.
 */
object ScreenMapper {

    fun map(dto: SduiScreenDto): ScreenDefinition {
        return ScreenDefinition(
            screenId = dto.screenId,
            schemaVersion = dto.schemaVersion,
            title = dto.title ?: "",
            root = mapComponent(dto.root),
            theme = dto.theme,
            initialState = dto.initialState
        )
    }

    fun mapComponent(dto: SduiComponentDto): SduiNode {
        val modifiers = dto.modifiers.map { mapModifier(it) }
        val children = dto.children.map { mapComponent(it) }

        return when (dto.type) {
            "Column" -> SduiNode.ColumnNode(
                space = dto.intProp("space", -1),
                verticalArrangement = dto.stringProp("verticalArrangement"),
                horizontalAlignment = dto.stringProp("horizontalAlignment"),
                children = children,
                modifiers = modifiers
            )
            "Row" -> SduiNode.RowNode(
                space = dto.intProp("space", -1),
                horizontalArrangement = dto.stringProp("horizontalArrangement"),
                verticalAlignment = dto.stringProp("verticalAlignment"),
                children = children,
                modifiers = modifiers
            )
            "Box" -> SduiNode.BoxNode(
                contentAlignment = dto.stringProp("contentAlignment"),
                children = children,
                modifiers = modifiers
            )
            "Spacer" -> SduiNode.SpacerNode(
                modifiers = modifiers
            )
            "Text" -> SduiNode.TextNode(
                text = dto.stringProp("text"),
                style = dto.styleProp()?.let { mapStyle(it) },
                modifiers = modifiers
            )
            "Image" -> SduiNode.ImageNode(
                url = dto.stringProp("url"),
                contentDescription = dto.stringProp("contentDescription"),
                contentScale = dto.stringProp("contentScale"),
                modifiers = modifiers
            )
            "Icon" -> SduiNode.IconNode(
                url = dto.stringProp("url"),
                name = dto.stringProp("name"),
                tint = dto.stringProp("tint"),
                modifiers = modifiers
            )
            "Divider" -> SduiNode.DividerNode(
                thickness = dto.intProp("thickness", 1),
                color = dto.stringProp("color"),
                modifiers = modifiers
            )
            "Card" -> SduiNode.CardNode(
                elevation = dto.intProp("elevation", 1),
                shape = dto.stringProp("shape"),
                radius = dto.intProp("radius", 8),
                children = children,
                modifiers = modifiers
            )
            "Surface" -> SduiNode.SurfaceNode(
                color = dto.stringProp("color"),
                elevation = dto.intProp("elevation", 0),
                shape = dto.stringProp("shape"),
                radius = dto.intProp("radius", 0),
                contentColor = dto.stringProp("contentColor"),
                shadowElevation = dto.intProp("shadowElevation", 0),
                borderWidth = dto.intProp("borderWidth", 0),
                borderColor = dto.stringProp("borderColor"),
                children = children,
                modifiers = modifiers
            )
            "Button" -> SduiNode.ButtonNode(
                text = dto.stringProp("text"),
                enabled = dto.booleanProp("enabled", true),
                onClick = dto.actions["onClick"]?.let { mapAction(it) },
                children = children,
                modifiers = modifiers
            )
            "TextField" -> SduiNode.TextFieldNode(
                stateKey = dto.stringProp("stateKey"),
                label = dto.stringProp("label"),
                placeholder = dto.stringProp("placeholder"),
                enabled = dto.booleanProp("enabled", true),
                singleLine = dto.booleanProp("singleLine", true),
                keyboardType = dto.stringProp("keyboardType", "Text"),
                visualTransformation = dto.stringProp("visualTransformation"),
                modifiers = modifiers
            )
            "Switch" -> SduiNode.SwitchNode(
                stateKey = dto.stringProp("stateKey"),
                enabled = dto.booleanProp("enabled", true),
                modifiers = modifiers
            )
            "Checkbox" -> SduiNode.CheckboxNode(
                stateKey = dto.stringProp("stateKey"),
                enabled = dto.booleanProp("enabled", true),
                modifiers = modifiers
            )
            "RadioButton" -> SduiNode.RadioButtonNode(
                stateKey = dto.stringProp("stateKey"),
                valueStr = dto.props["value"]?.toString() ?: "",
                enabled = dto.booleanProp("enabled", true),
                modifiers = modifiers
            )
            "Slider" -> SduiNode.SliderNode(
                stateKey = dto.stringProp("stateKey"),
                valueRangeMin = dto.props["valueRangeMin"]?.jsonPrimitive?.floatOrNull ?: 0f,
                valueRangeMax = dto.props["valueRangeMax"]?.jsonPrimitive?.floatOrNull ?: 1f,
                steps = dto.intProp("steps", 0),
                enabled = dto.booleanProp("enabled", true),
                modifiers = modifiers
            )
            "DropdownMenu" -> {
                val optsElement = dto.props["options"]
                val optionsList = if (optsElement is JsonArray) {
                    optsElement.map { it.jsonPrimitive.content }
                } else emptyList()
                SduiNode.DropdownMenuNode(
                    stateKey = dto.stringProp("stateKey"),
                    label = dto.stringProp("label"),
                    options = optionsList,
                    enabled = dto.booleanProp("enabled", true),
                    modifiers = modifiers
                )
            }
            "LazyColumn" -> SduiNode.LazyColumnNode(
                space = dto.intProp("space", -1),
                verticalArrangement = dto.stringProp("verticalArrangement"),
                horizontalAlignment = dto.stringProp("horizontalAlignment"),
                children = children,
                modifiers = modifiers
            )
            "LazyRow" -> SduiNode.LazyRowNode(
                space = dto.intProp("space", -1),
                horizontalArrangement = dto.stringProp("horizontalArrangement"),
                verticalAlignment = dto.stringProp("verticalAlignment"),
                children = children,
                modifiers = modifiers
            )
            "LazyGrid" -> SduiNode.LazyGridNode(
                columns = dto.intProp("columns", 2),
                space = dto.intProp("space", 0),
                children = children,
                modifiers = modifiers
            )
            "Conditional" -> {
                val thenComp = dto.props["then"]?.let {
                    try {
                        mapComponent(Json.decodeFromJsonElement(SduiComponentDto.serializer(), it))
                    } catch (e: Exception) {
                        null
                    }
                }
                val elseComp = dto.props["else"]?.let {
                    try {
                        mapComponent(Json.decodeFromJsonElement(SduiComponentDto.serializer(), it))
                    } catch (e: Exception) {
                        null
                    }
                }
                SduiNode.ConditionalNode(
                    stateKey = dto.stringProp("stateKey"),
                    operator = dto.stringProp("operator"),
                    compareValueStr = dto.props["compareValue"]?.toString(),
                    thenComponent = thenComp,
                    elseComponent = elseComp,
                    children = children,
                    modifiers = modifiers
                )
            }
            "Visible" -> SduiNode.VisibleNode(
                stateKey = dto.stringProp("stateKey"),
                operator = dto.stringProp("operator"),
                compareValueStr = dto.props["compareValue"]?.toString(),
                children = children,
                modifiers = modifiers
            )
            "Scaffold" -> SduiNode.ScaffoldNode(
                topBarTitle = dto.stringProp("topBarTitle"),
                topBarNavigationIcon = dto.stringProp("topBarNavigationIcon"),
                showTopBar = dto.booleanProp("showTopBar", false),
                children = children,
                modifiers = modifiers
            )
            "IconButton" -> SduiNode.IconButtonNode(
                icon = dto.stringProp("icon"),
                contentDescription = dto.stringProp("contentDescription"),
                enabled = dto.booleanProp("enabled", true),
                onClick = dto.actions["onClick"]?.let { mapAction(it) },
                modifiers = modifiers
            )
            "CircularProgress" -> SduiNode.CircularProgressNode(
                progress = dto.props["progress"]?.jsonPrimitive?.floatOrNull ?: -1f,
                color = dto.stringProp("color"),
                modifiers = modifiers
            )
            "LinearProgress" -> SduiNode.LinearProgressNode(
                progress = dto.props["progress"]?.jsonPrimitive?.floatOrNull ?: -1f,
                color = dto.stringProp("color"),
                trackColor = dto.stringProp("trackColor"),
                modifiers = modifiers
            )
            else -> SduiNode.FallbackNode(
                originalType = dto.type,
                originalProps = dto.props,
                originalActions = dto.actions.mapValues { mapAction(it.value) },
                children = children,
                modifiers = modifiers
            )
        }
    }

    fun mapModifier(dto: SduiModifierDto): SduiModifier {
        return SduiModifier(
            type = dto.type,
            valueStr = dto.value?.toString()
        )
    }

    fun mapAction(dto: SduiActionDto): SduiAction {
        return SduiAction(
            type = dto.type,
            route = dto.route,
            params = dto.params,
            url = dto.url,
            stateKey = dto.stateKey,
            valueStr = dto.value?.toString(),
            endpoint = dto.endpoint,
            method = dto.method,
            bodyStr = dto.body?.toString(),
            onSuccess = dto.onSuccess?.let { mapAction(it) },
            onError = dto.onError?.let { mapAction(it) },
            message = dto.message,
            actionLabel = dto.actionLabel,
            onAction = dto.onAction?.let { mapAction(it) },
            title = dto.title,
            confirmText = dto.confirmText,
            dismissText = dto.dismissText,
            onConfirm = dto.onConfirm?.let { mapAction(it) },
            onDismiss = dto.onDismiss?.let { mapAction(it) },
            name = dto.name,
            payload = dto.payload,
            actions = dto.actions.map { mapAction(it) },
            eventName = dto.eventName,
            operator = dto.operator,
            compareValueStr = dto.compareValue?.toString(),
            thenAction = dto.thenAction?.let { mapAction(it) },
            elseAction = dto.elseAction?.let { mapAction(it) }
        )
    }

    fun mapActionToDto(action: SduiAction): SduiActionDto {
        return SduiActionDto(
            type = action.type,
            route = action.route,
            params = action.params,
            url = action.url,
            stateKey = action.stateKey,
            value = action.valueStr?.let { Json.parseToJsonElement(it) },
            endpoint = action.endpoint,
            method = action.method,
            body = action.bodyStr?.let { Json.parseToJsonElement(it) },
            onSuccess = action.onSuccess?.let { mapActionToDto(it) },
            onError = action.onError?.let { mapActionToDto(it) },
            message = action.message,
            actionLabel = action.actionLabel,
            onAction = action.onAction?.let { mapActionToDto(it) },
            title = action.title,
            confirmText = action.confirmText,
            dismissText = action.dismissText,
            onConfirm = action.onConfirm?.let { mapActionToDto(it) },
            onDismiss = action.onDismiss?.let { mapActionToDto(it) },
            name = action.name,
            payload = action.payload,
            actions = action.actions.map { mapActionToDto(it) },
            eventName = action.eventName,
            operator = action.operator,
            compareValue = action.compareValueStr?.let { Json.parseToJsonElement(it) },
            thenAction = action.thenAction?.let { mapActionToDto(it) },
            elseAction = action.elseAction?.let { mapActionToDto(it) }
        )
    }

    fun mapStyle(dto: SduiStyleDto): SduiStyle {
        return SduiStyle(
            fontSize = dto.fontSize,
            fontWeight = dto.fontWeight,
            fontStyle = dto.fontStyle,
            color = dto.color,
            textAlign = dto.textAlign,
            maxLines = dto.maxLines,
            overflow = dto.overflow,
            letterSpacing = dto.letterSpacing,
            lineHeight = dto.lineHeight,
            textDecoration = dto.textDecoration
        )
    }
}
