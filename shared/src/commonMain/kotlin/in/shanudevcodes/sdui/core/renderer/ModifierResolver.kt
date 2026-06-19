package `in`.shanudevcodes.sdui.core.renderer

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.captionBarPadding
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.layout.waterfallPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import `in`.shanudevcodes.sdui.core.schema.SduiActionDto
import `in`.shanudevcodes.sdui.core.schema.SduiModifierDto
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiModifier
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiAction
import `in`.shanudevcodes.sdui.feature.screen.data.mapper.ScreenMapper
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Utility to resolve server-defined modifiers into native Compose Modifiers.
 */
object ModifierResolver {

    /**
     * Resolves a list of domain SduiModifier elements into a single chained Compose Modifier.
     */
    fun resolveDomain(
        modifiers: List<SduiModifier>,
        onAction: (SduiAction) -> Unit
    ): Modifier {
        val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
        val dtoModifiers = modifiers.map { mod ->
            SduiModifierDto(
                type = mod.type,
                value = mod.valueStr?.let {
                    try {
                        json.parseToJsonElement(it)
                    } catch (e: Exception) {
                        null
                    }
                }
            )
        }
        return resolve(dtoModifiers) { actionDto ->
            onAction(ScreenMapper.mapAction(actionDto))
        }
    }

    /**
     * Resolves a list of SduiModifierDto elements into a single chained Compose Modifier.
     */
    fun resolve(
        modifiers: List<SduiModifierDto>,
        onAction: (SduiActionDto) -> Unit
    ): Modifier {
        var result: Modifier = Modifier
        for (modifier in modifiers) {
            result = when (modifier.type) {
                "padding" -> result.applyPadding(modifier.value)
                "width" -> {
                    val w = modifier.value?.jsonPrimitive?.intOrNull ?: 0
                    result.width(w.dp)
                }
                "height" -> {
                    val h = modifier.value?.jsonPrimitive?.intOrNull ?: 0
                    result.height(h.dp)
                }
                "size" -> result.applySize(modifier.value)
                "fillMaxWidth" -> {
                    val fraction = modifier.value?.jsonPrimitive?.floatOrNull ?: 1.0f
                    result.fillMaxWidth(fraction)
                }
                "fillMaxHeight" -> {
                    val fraction = modifier.value?.jsonPrimitive?.floatOrNull ?: 1.0f
                    result.fillMaxHeight(fraction)
                }
                "fillMaxSize" -> {
                    val fraction = modifier.value?.jsonPrimitive?.floatOrNull ?: 1.0f
                    result.fillMaxSize(fraction)
                }
                "background" -> {
                    val hex = modifier.value?.jsonPrimitive?.content ?: ""
                    val color = parseHexColor(hex)
                    if (color != Color.Unspecified) result.background(color) else result
                }
                "backgroundGradient" -> result.applyGradientBackground(modifier.value)
                "border" -> result.applyBorder(modifier.value)
                "clip" -> result.clip(parseShape(modifier.value))
                "shadow" -> result.applyShadow(modifier.value)
                "alpha" -> {
                    val a = modifier.value?.jsonPrimitive?.floatOrNull ?: 1.0f
                    result.alpha(a)
                }
                "aspectRatio" -> {
                    val ratio = modifier.value?.jsonPrimitive?.floatOrNull ?: 1.0f
                    result.aspectRatio(ratio)
                }
                "clickable" -> result.applyClickable(modifier.value, onAction)
                "weight" -> {
                    // Weight is scope-dependent (RowScope/ColumnScope) and is ignored in the global chain.
                    // Row and Column renderers will inspect the modifier list and apply weight independently.
                    result
                }
                "wrapContentWidth" -> result.wrapContentWidth()
                "wrapContentHeight" -> result.wrapContentHeight()
                "scrollable" -> {
                    // Scrollable is handled at the Column/Row renderer level via a "scrollable" prop.
                    result
                }
                "testTag" -> {
                    val tag = modifier.value?.jsonPrimitive?.content ?: ""
                    if (tag.isNotEmpty()) result.testTag(tag) else result
                }
                "semantics" -> {
                    val desc = (modifier.value as? JsonObject)
                        ?.get("contentDescription")?.jsonPrimitive?.content ?: ""
                    if (desc.isNotEmpty()) result.semantics { contentDescription = desc } else result
                }
                "semanticsButton" -> {
                    result.semantics { role = Role.Button }
                }
                // System inset modifiers — no value needed, each maps to a WindowInsets extension
                "statusBarsPadding"     -> result.statusBarsPadding()
                "navigationBarsPadding" -> result.navigationBarsPadding()
                "systemBarsPadding"     -> result.systemBarsPadding()
                "imePadding"            -> result.imePadding()
                "safeDrawingPadding"    -> result.safeDrawingPadding()
                "safeContentPadding"    -> result.safeContentPadding()
                "captionBarPadding"     -> result.captionBarPadding()
                "displayCutoutPadding"  -> result.displayCutoutPadding()
                "systemGesturesPadding" -> result.systemGesturesPadding()
                "waterfallPadding"      -> result.waterfallPadding()
                else -> {
                    // Unknown modifier, skip gracefully
                    result
                }
            }
        }
        return result
    }

    private fun Modifier.applyGradientBackground(value: JsonElement?): Modifier {
        if (value == null || value !is JsonObject) return this
        val obj = value.jsonObject
        val colorsArray = obj["colors"]
        val colors = if (colorsArray != null) {
            try {
                val json = kotlinx.serialization.json.Json
                val list = colorsArray as? kotlinx.serialization.json.JsonArray ?: return this
                list.mapNotNull { el ->
                    val hex = (el as? JsonPrimitive)?.content ?: return@mapNotNull null
                    val c = parseHexColor(hex)
                    if (c != Color.Unspecified) c else null
                }
            } catch (e: Exception) { emptyList() }
        } else emptyList()
        if (colors.size < 2) return this
        val direction = obj["direction"]?.jsonPrimitive?.content ?: "vertical"
        val brush = when (direction) {
            "horizontal" -> Brush.horizontalGradient(colors)
            "diagonal"   -> Brush.linearGradient(colors)
            else          -> Brush.verticalGradient(colors)
        }
        return this.background(brush)
    }

    private fun Modifier.applyPadding(value: JsonElement?): Modifier {
        if (value == null) return this
        if (value is JsonPrimitive) {
            val all = value.intOrNull ?: 0
            return this.padding(all.dp)
        }
        if (value is JsonObject) {
            val obj = value.jsonObject
            val all = obj["all"]?.jsonPrimitive?.intOrNull
            if (all != null) {
                return this.padding(all.dp)
            }
            val horizontal = obj["horizontal"]?.jsonPrimitive?.intOrNull
            val vertical = obj["vertical"]?.jsonPrimitive?.intOrNull
            if (horizontal != null || vertical != null) {
                return this.padding(
                    horizontal = (horizontal ?: 0).dp,
                    vertical = (vertical ?: 0).dp
                )
            }
            val start = obj["start"]?.jsonPrimitive?.intOrNull ?: 0
            val top = obj["top"]?.jsonPrimitive?.intOrNull ?: 0
            val end = obj["end"]?.jsonPrimitive?.intOrNull ?: 0
            val bottom = obj["bottom"]?.jsonPrimitive?.intOrNull ?: 0
            return this.padding(start.dp, top.dp, end.dp, bottom.dp)
        }
        return this
    }

    private fun Modifier.applySize(value: JsonElement?): Modifier {
        if (value == null) return this
        if (value is JsonPrimitive) {
            val s = value.intOrNull ?: 0
            return this.size(s.dp)
        }
        if (value is JsonObject) {
            val obj = value.jsonObject
            val w = obj["width"]?.jsonPrimitive?.intOrNull ?: 0
            val h = obj["height"]?.jsonPrimitive?.intOrNull ?: 0
            return this.size(w.dp, h.dp)
        }
        return this
    }

    private fun Modifier.applyBorder(value: JsonElement?): Modifier {
        if (value == null || value !is JsonObject) return this
        val obj = value.jsonObject
        val width = obj["width"]?.jsonPrimitive?.intOrNull ?: 1
        val hexColor = obj["color"]?.jsonPrimitive?.content ?: ""
        val color = parseHexColor(hexColor)
        val shape = parseShape(obj["shape"])
        return if (color != Color.Unspecified) {
            this.border(width.dp, color, shape)
        } else this
    }

    private fun Modifier.applyShadow(value: JsonElement?): Modifier {
        if (value == null) return this
        if (value is JsonPrimitive) {
            val e = value.intOrNull ?: 0
            return this.shadow(e.dp)
        }
        if (value is JsonObject) {
            val obj = value.jsonObject
            val elevation = obj["elevation"]?.jsonPrimitive?.intOrNull ?: 0
            val shape = parseShape(obj["shape"])
            return this.shadow(elevation.dp, shape)
        }
        return this
    }

    private fun Modifier.applyClickable(value: JsonElement?, onAction: (SduiActionDto) -> Unit): Modifier {
        if (value == null) return this
        return try {
            val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
            val actionDto = json.decodeFromJsonElement(SduiActionDto.serializer(), value)
            this.clickable { onAction(actionDto) }
        } catch (e: Exception) {
            this
        }
    }

    private fun parseShape(value: JsonElement?): Shape {
        if (value == null) return RectangleShape
        if (value is JsonPrimitive) {
            return when (value.content) {
                "Circle" -> CircleShape
                else -> RectangleShape
            }
        }
        if (value is JsonObject) {
            val obj = value.jsonObject
            val shapeType = obj["shape"]?.jsonPrimitive?.content
            return when (shapeType) {
                "Circle" -> CircleShape
                "RoundedCorner" -> {
                    val radius = obj["radius"]?.jsonPrimitive?.intOrNull ?: 0
                    RoundedCornerShape(radius.dp)
                }
                else -> RectangleShape
            }
        }
        return RectangleShape
    }

    private fun parseHexColor(hexColorString: String): Color {
        val cleanHex = hexColorString.removePrefix("#")
        return try {
            when (cleanHex.length) {
                6 -> Color(cleanHex.toLong(16) or 0xFF000000)
                8 -> Color(cleanHex.toLong(16))
                else -> Color.Unspecified
            }
        } catch (e: Exception) {
            Color.Unspecified
        }
    }
}
