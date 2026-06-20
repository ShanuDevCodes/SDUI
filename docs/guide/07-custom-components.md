# 07 — Custom components

Register your own native composables and reference them from JSON by `type`. This is how you extend SDUI
with app-specific UI (sponsor banners, product cards, native widgets) that the server can place into the
tree like any built-in.

---

## Registering a component

Call `SduiEngine.registerComponent(type, renderer)` once, alongside engine initialization. The renderer
receives the node DTO, the resolved modifier, and the screen's state holder.

```kotlin
import `in`.shanudevcodes.sdui.core.engine.SduiEngine
import `in`.shanudevcodes.sdui.core.renderer.LocalSduiActionHandler
import `in`.shanudevcodes.sdui.core.schema.action
import `in`.shanudevcodes.sdui.core.schema.booleanProp
import `in`.shanudevcodes.sdui.core.schema.intProp
import `in`.shanudevcodes.sdui.core.schema.stringProp

remember {
    SduiEngine.initialize(SduiConfig(baseUrl = "https://api.example.com/"))

    SduiEngine.registerComponent("SponsorBanner") { component, modifier, stateHolder ->
        val onAction = LocalSduiActionHandler.current
        SponsorBannerView(
            imageUrl = component.stringProp("imageUrl"),
            title    = component.stringProp("title"),
            featured = component.booleanProp("featured"),
            onClick  = { component.action("onClick")?.let(onAction) },
            modifier = modifier
        )
    }
}
```

The renderer signature is:

```kotlin
typealias ComponentRenderer = @Composable (
    component: SduiComponentDto,
    modifier: Modifier,
    stateHolder: SduiStateHolder
) -> Unit
```

---

## Referencing it from JSON

```json
{
  "type": "SponsorBanner",
  "props": { "imageUrl": "https://cdn.example.com/promo.png", "title": "Summer sale", "featured": true },
  "modifiers": [ { "type": "fillMaxWidth" }, { "type": "padding", "value": 16 } ],
  "actions": { "onClick": { "type": "Navigate", "route": "shop" } }
}
```

The `modifier` passed to your renderer already includes the resolved `modifiers` list, so apply it to your
root layout. `props` and `actions` are read with the helpers below.

---

## Reading props

Import the extension helpers from `in.shanudevcodes.sdui.core.schema`:

| Helper | Signature | Fallback |
|--------|-----------|----------|
| `stringProp` | `component.stringProp("key", default = "")` | `""` |
| `intProp` | `component.intProp("key", default = 0)` | `0` |
| `booleanProp` | `component.booleanProp("key", default = false)` | `false` |
| `action` | `component.action("key"): SduiActionDto?` | `null` |

For raw access, `component.props` is a `Map<String, JsonElement>` and `component.children` is the list of
child nodes (you can render them yourself if your component is a container).

---

## Dispatching actions

Read the current action handler from the composition and invoke it with the action from the `actions` map:

```kotlin
val onAction = LocalSduiActionHandler.current
// ...
onClick = { component.action("onClick")?.let(onAction) }
```

`LocalSduiActionHandler` routes through the same pipeline as built-in components, including your
`actionInterceptor`. See [05 — Actions & state](./05-actions-and-state.md).

---

## Reacting to state

`stateHolder` is the screen's reactive store. Read it as Compose state:

```kotlin
SduiEngine.registerComponent("LiveCounter") { component, modifier, stateHolder ->
    val state by stateHolder.state.collectAsState()
    val key = component.stringProp("stateKey")
    val count = state[key]?.jsonPrimitive?.intOrNull ?: 0
    Text("Count: $count", modifier = modifier)
}
```

To write state, call `stateHolder.setValue(key, JsonPrimitive(...))`.

---

## Rendering child nodes

If your custom component is a container, render its children via the engine's renderer:

```kotlin
import `in`.shanudevcodes.sdui.core.renderer.SduiRenderer

SduiEngine.registerComponent("Carousel") { component, modifier, stateHolder ->
    LazyRow(modifier) {
        items(component.children) { child ->
            SduiRenderer(component = child, stateHolder = stateHolder)
        }
    }
}
```

> `SduiRenderer` is public so custom containers can recurse into children using the same engine pipeline
> (modifiers, depth guard, action handler).

---

## Overriding built-ins

Registering a `type` that matches a built-in name **replaces** the built-in for the whole app. For example,
registering `"Button"` makes every `Button` node use your composable. Use this sparingly and intentionally.

```kotlin
SduiEngine.registerComponent("Button") { component, modifier, stateHolder ->
    BrandButton(text = component.stringProp("text"), modifier = modifier) { /* ... */ }
}
```

---

## Tips

- Register components **before** any screen that uses them renders (do it during init).
- Keep the `type` names stable — they are a contract with your server JSON.
- Validate/default missing props with the helper defaults so malformed payloads degrade gracefully.
- Custom components are a host-app concern; they are not part of the published library API surface.
