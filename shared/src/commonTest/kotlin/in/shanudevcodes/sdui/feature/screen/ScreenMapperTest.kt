package `in`.shanudevcodes.sdui.feature.screen

import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.SduiScreenDto
import `in`.shanudevcodes.sdui.core.schema.SduiStyleDto
import `in`.shanudevcodes.sdui.feature.screen.data.mapper.ScreenMapper
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiStyle
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests verifying mapping of SDUI Screen DTO models to Domain representations.
 */
class ScreenMapperTest {

    @Test
    fun testScreenMapper_mapsRootsCorrectly() {
        val rootDto = SduiComponentDto(
            type = "Text",
            props = mapOf(
                "text" to JsonPrimitive("Hello Mapper")
            )
        )
        val screenDto = SduiScreenDto(
            screenId = "test_screen",
            schemaVersion = "1.0.0",
            title = "Test Title",
            root = rootDto
        )

        val definition = ScreenMapper.map(screenDto)

        assertEquals("test_screen", definition.screenId)
        assertEquals("1.0.0", definition.schemaVersion)
        assertEquals("Test Title", definition.title)
        assertTrue(definition.root is SduiNode.TextNode)
        assertEquals("Hello Mapper", (definition.root as SduiNode.TextNode).text)
    }

    @Test
    fun testScreenMapper_preservesInitialState() {
        val rootDto = SduiComponentDto(type = "Spacer")
        val initialState = mapOf(
            "counter" to JsonPrimitive(10),
            "username" to JsonPrimitive("Shanu")
        )
        val screenDto = SduiScreenDto(
            screenId = "test_screen",
            schemaVersion = "1.0.0",
            root = rootDto,
            initialState = initialState
        )

        val definition = ScreenMapper.map(screenDto)
        assertEquals(initialState, definition.initialState)
    }

    @Test
    fun testScreenMapper_mapsSurfaceAndCardNodes() {
        val surfaceDto = SduiComponentDto(
            type = "Surface",
            props = mapOf(
                "color" to JsonPrimitive("#FFFFFF"),
                "elevation" to JsonPrimitive(4),
                "shape" to JsonPrimitive("RoundedCorner"),
                "radius" to JsonPrimitive(12),
                "contentColor" to JsonPrimitive("#000000"),
                "shadowElevation" to JsonPrimitive(6),
                "borderWidth" to JsonPrimitive(2),
                "borderColor" to JsonPrimitive("#FF0000")
            )
        )

        val node = ScreenMapper.mapComponent(surfaceDto)
        assertTrue(node is SduiNode.SurfaceNode)
        assertEquals("#FFFFFF", node.color)
        assertEquals(4, node.elevation)
        assertEquals("RoundedCorner", node.shape)
        assertEquals(12, node.radius)
        assertEquals("#000000", node.contentColor)
        assertEquals(6, node.shadowElevation)
        assertEquals(2, node.borderWidth)
        assertEquals("#FF0000", node.borderColor)

        val cardDto = SduiComponentDto(
            type = "Card",
            props = mapOf(
                "elevation" to JsonPrimitive(2),
                "shape" to JsonPrimitive("CutCorner"),
                "radius" to JsonPrimitive(8)
            )
        )
        val cardNode = ScreenMapper.mapComponent(cardDto)
        assertTrue(cardNode is SduiNode.CardNode)
        assertEquals(2, cardNode.elevation)
        assertEquals("CutCorner", cardNode.shape)
        assertEquals(8, cardNode.radius)
    }

    @Test
    fun testScreenMapper_mapsNewNodeTypes() {
        // Scaffold
        val scaffoldDto = SduiComponentDto(
            type = "Scaffold",
            props = mapOf(
                "topBarTitle" to JsonPrimitive("Home Title"),
                "topBarNavigationIcon" to JsonPrimitive("back"),
                "showTopBar" to JsonPrimitive(true)
            )
        )
        val scaffoldNode = ScreenMapper.mapComponent(scaffoldDto)
        assertTrue(scaffoldNode is SduiNode.ScaffoldNode)
        assertEquals("Home Title", scaffoldNode.topBarTitle)
        assertEquals("back", scaffoldNode.topBarNavigationIcon)
        assertEquals(true, scaffoldNode.showTopBar)

        // IconButton
        val iconButtonDto = SduiComponentDto(
            type = "IconButton",
            props = mapOf(
                "icon" to JsonPrimitive("favorite"),
                "contentDescription" to JsonPrimitive("Like"),
                "enabled" to JsonPrimitive(false)
            )
        )
        val iconButtonNode = ScreenMapper.mapComponent(iconButtonDto)
        assertTrue(iconButtonNode is SduiNode.IconButtonNode)
        assertEquals("favorite", iconButtonNode.icon)
        assertEquals("Like", iconButtonNode.contentDescription)
        assertEquals(false, iconButtonNode.enabled)

        // CircularProgress
        val circularProgressDto = SduiComponentDto(
            type = "CircularProgress",
            props = mapOf(
                "progress" to JsonPrimitive(0.75f),
                "color" to JsonPrimitive("#FFCC00")
            )
        )
        val circularProgressNode = ScreenMapper.mapComponent(circularProgressDto)
        assertTrue(circularProgressNode is SduiNode.CircularProgressNode)
        assertEquals(0.75f, circularProgressNode.progress)
        assertEquals("#FFCC00", circularProgressNode.color)

        // LinearProgress
        val linearProgressDto = SduiComponentDto(
            type = "LinearProgress",
            props = mapOf(
                "progress" to JsonPrimitive(0.3f),
                "color" to JsonPrimitive("#FFCC00"),
                "trackColor" to JsonPrimitive("#CCCCCC")
            )
        )
        val linearProgressNode = ScreenMapper.mapComponent(linearProgressDto)
        assertTrue(linearProgressNode is SduiNode.LinearProgressNode)
        assertEquals(0.3f, linearProgressNode.progress)
        assertEquals("#FFCC00", linearProgressNode.color)
        assertEquals("#CCCCCC", linearProgressNode.trackColor)

        // LazyGrid
        val lazyGridDto = SduiComponentDto(
            type = "LazyGrid",
            props = mapOf(
                "columns" to JsonPrimitive(3),
                "space" to JsonPrimitive(16)
            )
        )
        val lazyGridNode = ScreenMapper.mapComponent(lazyGridDto)
        assertTrue(lazyGridNode is SduiNode.LazyGridNode)
        assertEquals(3, lazyGridNode.columns)
        assertEquals(16, lazyGridNode.space)
    }

    @Test
    fun testSduiStyle_preservesLetterSpacingLineHeightTextDecoration() {
        val styleDto = SduiStyleDto(
            fontSize = 16,
            fontWeight = "Bold",
            fontStyle = "Italic",
            color = "#1A1A2E",
            textAlign = "Center",
            letterSpacing = 0.5f,
            lineHeight = 24,
            maxLines = 2,
            overflow = "Ellipsis",
            textDecoration = "Underline"
        )

        val style = ScreenMapper.mapStyle(styleDto)
        assertEquals(0.5f, style.letterSpacing)
        assertEquals(24, style.lineHeight)
        assertEquals("Underline", style.textDecoration)

        val textDto = SduiComponentDto(
            type = "Text",
            props = mapOf(
                "text" to JsonPrimitive("Style Test"),
                "style" to kotlinx.serialization.json.Json.encodeToJsonElement(SduiStyleDto.serializer(), styleDto)
            )
        )

        val node = ScreenMapper.mapComponent(textDto)
        assertTrue(node is SduiNode.TextNode)
        assertNotNull(node.style)
        assertEquals(0.5f, node.style?.letterSpacing)
        assertEquals(24, node.style?.lineHeight)
        assertEquals("Underline", node.style?.textDecoration)
    }
}

