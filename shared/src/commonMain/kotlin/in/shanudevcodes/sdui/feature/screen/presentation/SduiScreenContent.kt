package `in`.shanudevcodes.sdui.feature.screen.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.CompositionLocalProvider
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer
import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.state.SduiStateHolder
import `in`.shanudevcodes.sdui.core.theme.SduiThemeResolver
import `in`.shanudevcodes.sdui.feature.screen.data.mapper.ScreenMapper
import `in`.shanudevcodes.sdui.feature.screen.domain.model.ScreenDefinition
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiAction

/**
 * Stateless view container that takes a ScreenDefinition and delegates rendering to the recursive layout engine.
 */
@Composable
fun SduiScreenContent(
    definition: ScreenDefinition,
    stateHolder: SduiStateHolder,
    onAction: (SduiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val rootDto = mapNodeToDto(definition.root)
    SduiThemeResolver.SduiTheme(theme = definition.theme) {
        CompositionLocalProvider(
            LocalSduiActionHandler provides { actionDto ->
                onAction(ScreenMapper.mapAction(actionDto))
            }
        ) {
            SduiRenderer(component = rootDto, stateHolder = stateHolder, modifier = modifier)
        }
    }
}

private fun mapNodeToDto(node: `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode): SduiComponentDto {
    val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
    val props = mutableMapOf<String, kotlinx.serialization.json.JsonElement>()
    val actions = mutableMapOf<String, `in`.shanudevcodes.sdui.core.schema.SduiActionDto>()

    fun mapActionToDto(action: `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiAction): `in`.shanudevcodes.sdui.core.schema.SduiActionDto {
        return `in`.shanudevcodes.sdui.core.schema.SduiActionDto(
            type = action.type,
            route = action.route,
            params = action.params,
            url = action.url,
            stateKey = action.stateKey,
            value = action.valueStr?.let { json.parseToJsonElement(it) },
            endpoint = action.endpoint,
            method = action.method,
            body = action.bodyStr?.let { json.parseToJsonElement(it) },
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
            compareValue = action.compareValueStr?.let { json.parseToJsonElement(it) },
            thenAction = action.thenAction?.let { mapActionToDto(it) },
            elseAction = action.elseAction?.let { mapActionToDto(it) }
        )
    }

    when (node) {
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.ColumnNode -> {
            if (node.space >= 0) props["space"] = kotlinx.serialization.json.JsonPrimitive(node.space)
            if (node.verticalArrangement.isNotEmpty()) props["verticalArrangement"] = kotlinx.serialization.json.JsonPrimitive(node.verticalArrangement)
            if (node.horizontalAlignment.isNotEmpty()) props["horizontalAlignment"] = kotlinx.serialization.json.JsonPrimitive(node.horizontalAlignment)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.RowNode -> {
            if (node.space >= 0) props["space"] = kotlinx.serialization.json.JsonPrimitive(node.space)
            if (node.horizontalArrangement.isNotEmpty()) props["horizontalArrangement"] = kotlinx.serialization.json.JsonPrimitive(node.horizontalArrangement)
            if (node.verticalAlignment.isNotEmpty()) props["verticalAlignment"] = kotlinx.serialization.json.JsonPrimitive(node.verticalAlignment)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.BoxNode -> {
            if (node.contentAlignment.isNotEmpty()) props["contentAlignment"] = kotlinx.serialization.json.JsonPrimitive(node.contentAlignment)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.TextNode -> {
            props["text"] = kotlinx.serialization.json.JsonPrimitive(node.text)
            node.style?.let { style ->
                val styleDto = `in`.shanudevcodes.sdui.core.schema.SduiStyleDto(
                    fontSize = style.fontSize,
                    fontWeight = style.fontWeight,
                    fontStyle = style.fontStyle,
                    color = style.color,
                    textAlign = style.textAlign,
                    maxLines = style.maxLines,
                    overflow = style.overflow,
                    letterSpacing = style.letterSpacing,
                    lineHeight = style.lineHeight,
                    textDecoration = style.textDecoration
                )
                props["style"] = json.encodeToJsonElement(`in`.shanudevcodes.sdui.core.schema.SduiStyleDto.serializer(), styleDto)
            }
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.ImageNode -> {
            props["url"] = kotlinx.serialization.json.JsonPrimitive(node.url)
            props["contentDescription"] = kotlinx.serialization.json.JsonPrimitive(node.contentDescription)
            props["contentScale"] = kotlinx.serialization.json.JsonPrimitive(node.contentScale)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.IconNode -> {
            props["name"] = kotlinx.serialization.json.JsonPrimitive(node.name)
            props["tint"] = kotlinx.serialization.json.JsonPrimitive(node.tint)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.DividerNode -> {
            props["thickness"] = kotlinx.serialization.json.JsonPrimitive(node.thickness)
            props["color"] = kotlinx.serialization.json.JsonPrimitive(node.color)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.CardNode -> {
            props["elevation"] = kotlinx.serialization.json.JsonPrimitive(node.elevation)
            props["shape"] = kotlinx.serialization.json.JsonPrimitive(node.shape)
            props["radius"] = kotlinx.serialization.json.JsonPrimitive(node.radius)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.SurfaceNode -> {
            props["color"] = kotlinx.serialization.json.JsonPrimitive(node.color)
            props["elevation"] = kotlinx.serialization.json.JsonPrimitive(node.elevation)
            props["shape"] = kotlinx.serialization.json.JsonPrimitive(node.shape)
            props["radius"] = kotlinx.serialization.json.JsonPrimitive(node.radius)
            if (node.contentColor.isNotEmpty()) props["contentColor"] = kotlinx.serialization.json.JsonPrimitive(node.contentColor)
            if (node.shadowElevation > 0) props["shadowElevation"] = kotlinx.serialization.json.JsonPrimitive(node.shadowElevation)
            if (node.borderWidth > 0) props["borderWidth"] = kotlinx.serialization.json.JsonPrimitive(node.borderWidth)
            if (node.borderColor.isNotEmpty()) props["borderColor"] = kotlinx.serialization.json.JsonPrimitive(node.borderColor)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.ButtonNode -> {
            props["text"] = kotlinx.serialization.json.JsonPrimitive(node.text)
            props["enabled"] = kotlinx.serialization.json.JsonPrimitive(node.enabled)
            node.onClick?.let { actions["onClick"] = mapActionToDto(it) }
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.TextFieldNode -> {
            props["stateKey"] = kotlinx.serialization.json.JsonPrimitive(node.stateKey)
            props["label"] = kotlinx.serialization.json.JsonPrimitive(node.label)
            props["placeholder"] = kotlinx.serialization.json.JsonPrimitive(node.placeholder)
            props["enabled"] = kotlinx.serialization.json.JsonPrimitive(node.enabled)
            props["singleLine"] = kotlinx.serialization.json.JsonPrimitive(node.singleLine)
            props["keyboardType"] = kotlinx.serialization.json.JsonPrimitive(node.keyboardType)
            props["visualTransformation"] = kotlinx.serialization.json.JsonPrimitive(node.visualTransformation)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.SwitchNode -> {
            props["stateKey"] = kotlinx.serialization.json.JsonPrimitive(node.stateKey)
            props["enabled"] = kotlinx.serialization.json.JsonPrimitive(node.enabled)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.CheckboxNode -> {
            props["stateKey"] = kotlinx.serialization.json.JsonPrimitive(node.stateKey)
            props["enabled"] = kotlinx.serialization.json.JsonPrimitive(node.enabled)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.RadioButtonNode -> {
            props["stateKey"] = kotlinx.serialization.json.JsonPrimitive(node.stateKey)
            props["value"] = json.parseToJsonElement(node.valueStr)
            props["enabled"] = kotlinx.serialization.json.JsonPrimitive(node.enabled)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.SliderNode -> {
            props["stateKey"] = kotlinx.serialization.json.JsonPrimitive(node.stateKey)
            props["valueRangeMin"] = kotlinx.serialization.json.JsonPrimitive(node.valueRangeMin)
            props["valueRangeMax"] = kotlinx.serialization.json.JsonPrimitive(node.valueRangeMax)
            props["steps"] = kotlinx.serialization.json.JsonPrimitive(node.steps)
            props["enabled"] = kotlinx.serialization.json.JsonPrimitive(node.enabled)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.DropdownMenuNode -> {
            props["stateKey"] = kotlinx.serialization.json.JsonPrimitive(node.stateKey)
            props["label"] = kotlinx.serialization.json.JsonPrimitive(node.label)
            props["options"] = kotlinx.serialization.json.JsonArray(node.options.map { kotlinx.serialization.json.JsonPrimitive(it) })
            props["enabled"] = kotlinx.serialization.json.JsonPrimitive(node.enabled)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.LazyColumnNode -> {
            if (node.space >= 0) props["space"] = kotlinx.serialization.json.JsonPrimitive(node.space)
            if (node.verticalArrangement.isNotEmpty()) props["verticalArrangement"] = kotlinx.serialization.json.JsonPrimitive(node.verticalArrangement)
            if (node.horizontalAlignment.isNotEmpty()) props["horizontalAlignment"] = kotlinx.serialization.json.JsonPrimitive(node.horizontalAlignment)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.LazyRowNode -> {
            if (node.space >= 0) props["space"] = kotlinx.serialization.json.JsonPrimitive(node.space)
            if (node.horizontalArrangement.isNotEmpty()) props["horizontalArrangement"] = kotlinx.serialization.json.JsonPrimitive(node.horizontalArrangement)
            if (node.verticalAlignment.isNotEmpty()) props["verticalAlignment"] = kotlinx.serialization.json.JsonPrimitive(node.verticalAlignment)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.LazyGridNode -> {
            props["columns"] = kotlinx.serialization.json.JsonPrimitive(node.columns)
            props["space"] = kotlinx.serialization.json.JsonPrimitive(node.space)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.ConditionalNode -> {
            props["stateKey"] = kotlinx.serialization.json.JsonPrimitive(node.stateKey)
            props["operator"] = kotlinx.serialization.json.JsonPrimitive(node.operator)
            node.compareValueStr?.let { props["compareValue"] = json.parseToJsonElement(it) }
            node.thenComponent?.let { props["then"] = json.encodeToJsonElement(SduiComponentDto.serializer(), mapNodeToDto(it)) }
            node.elseComponent?.let { props["else"] = json.encodeToJsonElement(SduiComponentDto.serializer(), mapNodeToDto(it)) }
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.VisibleNode -> {
            props["stateKey"] = kotlinx.serialization.json.JsonPrimitive(node.stateKey)
            props["operator"] = kotlinx.serialization.json.JsonPrimitive(node.operator)
            node.compareValueStr?.let { props["compareValue"] = json.parseToJsonElement(it) }
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.SpacerNode -> {}
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.FallbackNode -> {}
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.ScaffoldNode -> {
            props["topBarTitle"] = kotlinx.serialization.json.JsonPrimitive(node.topBarTitle)
            props["topBarNavigationIcon"] = kotlinx.serialization.json.JsonPrimitive(node.topBarNavigationIcon)
            props["showTopBar"] = kotlinx.serialization.json.JsonPrimitive(node.showTopBar)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.IconButtonNode -> {
            props["icon"] = kotlinx.serialization.json.JsonPrimitive(node.icon)
            props["contentDescription"] = kotlinx.serialization.json.JsonPrimitive(node.contentDescription)
            props["enabled"] = kotlinx.serialization.json.JsonPrimitive(node.enabled)
            node.onClick?.let { actions["onClick"] = mapActionToDto(it) }
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.CircularProgressNode -> {
            props["progress"] = kotlinx.serialization.json.JsonPrimitive(node.progress)
            props["color"] = kotlinx.serialization.json.JsonPrimitive(node.color)
        }
        is `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode.LinearProgressNode -> {
            props["progress"] = kotlinx.serialization.json.JsonPrimitive(node.progress)
            props["color"] = kotlinx.serialization.json.JsonPrimitive(node.color)
            props["trackColor"] = kotlinx.serialization.json.JsonPrimitive(node.trackColor)
        }
    }

    return SduiComponentDto(
        type = node.type,
        props = props,
        modifiers = node.modifiers.map { mod ->
            `in`.shanudevcodes.sdui.core.schema.SduiModifierDto(
                type = mod.type,
                value = mod.valueStr?.let { json.parseToJsonElement(it) }
            )
        },
        children = node.children.map { mapNodeToDto(it) },
        actions = actions
    )
}
