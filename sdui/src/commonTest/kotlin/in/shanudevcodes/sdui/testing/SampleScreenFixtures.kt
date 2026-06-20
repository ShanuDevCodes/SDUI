package `in`.shanudevcodes.sdui.testing

/**
 * Static JSON payloads representing various SDUI screen configurations for testing.
 */
object SampleScreenFixtures {

    val SIMPLE_SCREEN_JSON = """
        {
          "screenId": "simple_screen",
          "schemaVersion": "1.0.0",
          "title": "Simple Title",
          "root": {
            "type": "Text",
            "props": {
              "text": "Hello World"
            }
          }
        }
    """.trimIndent()

    val THEMED_SCREEN_JSON = """
        {
          "screenId": "themed_screen",
          "schemaVersion": "1.0.0",
          "title": "Themed Title",
          "theme": {
            "colors": {
              "primary": "#FF6200EE",
              "background": "#FFFFFFFF",
              "surface": "#FFFFFFFF"
            }
          },
          "root": {
            "type": "Text",
            "props": {
              "text": "Themed Text"
            }
          }
        }
    """.trimIndent()

    val ACTIONS_SCREEN_JSON = """
        {
          "screenId": "actions_screen",
          "schemaVersion": "1.0.0",
          "title": "Actions Title",
          "root": {
            "type": "Button",
            "props": {
              "text": "Click Me"
            },
            "modifiers": [
              {
                "type": "Clickable",
                "action": {
                  "type": "Navigate",
                  "route": "profile_screen",
                  "params": {
                    "userId": "999"
                  }
                }
              }
            ]
          }
        }
    """.trimIndent()

    val LIST_SCREEN_JSON = """
        {
          "screenId": "list_screen",
          "schemaVersion": "1.0.0",
          "title": "Lazy List Title",
          "root": {
            "type": "LazyColumn",
            "children": [
              {
                "type": "Text",
                "props": {
                  "text": "Item 1"
                }
              },
              {
                "type": "Text",
                "props": {
                  "text": "Item 2"
                }
              }
            ]
          }
        }
    """.trimIndent()

    val MALFORMED_SCREEN_JSON = """
        {
          "screenId": "error_screen",
          "schemaVersion": "2.0.0",
          "title": "Error Screen"
        }
    """.trimIndent()
}
