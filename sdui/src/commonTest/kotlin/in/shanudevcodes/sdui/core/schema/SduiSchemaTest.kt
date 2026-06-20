package `in`.shanudevcodes.sdui.core.schema

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SduiSchemaTest {

    @Test
    fun testParseSduiScreen_success() {
        val jsonString = """
        {
          "screenId": "home_screen",
          "schemaVersion": "1.0.0",
          "title": "Home",
          "initialState": {
            "userName": "John Doe"
          },
          "root": {
            "type": "Column",
            "modifiers": [
              { "type": "padding", "value": 16 },
              { "type": "fillMaxSize" }
            ],
            "children": [
              {
                "type": "Text",
                "props": {
                  "text": "Welcome, {{userName}}!",
                  "style": {
                    "fontSize": 24,
                    "fontWeight": "Bold"
                  }
                }
              },
              {
                "type": "Button",
                "props": {
                  "text": "View Products"
                },
                "actions": {
                  "onClick": {
                    "type": "Navigate",
                    "route": "product_list",
                    "params": { "category": "featured" }
                  }
                }
              }
            ]
          }
        }
        """.trimIndent()

        val json = Json { ignoreUnknownKeys = true }
        val screen = json.decodeFromString<SduiScreenDto>(jsonString)

        assertEquals("home_screen", screen.screenId)
        assertEquals("1.0.0", screen.schemaVersion)
        assertEquals("Home", screen.title)
        assertEquals("Column", screen.root.type)
        assertEquals(2, screen.root.modifiers.size)
        assertEquals(2, screen.root.children.size)

        val textNode = screen.root.children[0]
        assertEquals("Text", textNode.type)

        val buttonNode = screen.root.children[1]
        assertEquals("Button", buttonNode.type)
        assertNotNull(buttonNode.actions["onClick"])
        assertEquals("Navigate", buttonNode.actions["onClick"]?.type)
        assertEquals("product_list", buttonNode.actions["onClick"]?.route)
    }
}
