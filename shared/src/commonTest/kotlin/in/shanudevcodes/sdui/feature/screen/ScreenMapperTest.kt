package `in`.shanudevcodes.sdui.feature.screen

import `in`.shanudevcodes.sdui.core.schema.SduiComponentDto
import `in`.shanudevcodes.sdui.core.schema.SduiScreenDto
import `in`.shanudevcodes.sdui.feature.screen.data.mapper.ScreenMapper
import `in`.shanudevcodes.sdui.feature.screen.domain.model.SduiNode
import kotlin.test.Test
import kotlin.test.assertEquals
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
                "text" to kotlinx.serialization.json.JsonPrimitive("Hello Mapper")
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
}
