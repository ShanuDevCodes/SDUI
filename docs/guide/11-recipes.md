# 11 — Recipes & cookbook

Complete, copy-pasteable screen examples for common patterns. Each is a full screen document you can serve
from `GET {baseUrl}sdui/screens/{screenId}`.

---

## A scrollable list of cards

```json
{
  "screenId": "feed",
  "schemaVersion": "1.0.0",
  "title": "Feed",
  "root": {
    "type": "LazyColumn",
    "modifiers": [ { "type": "fillMaxSize" }, { "type": "padding", "value": 16 } ],
    "props": { "space": 12 },
    "children": [
      {
        "type": "Card",
        "modifiers": [ { "type": "fillMaxWidth" } ],
        "props": { "shape": { "shape": "RoundedCorner", "radius": 16 }, "elevation": 2 },
        "children": [
          {
            "type": "Column",
            "modifiers": [ { "type": "padding", "value": 16 } ],
            "props": { "space": 6 },
            "children": [
              { "type": "Text", "props": { "text": "Card title", "style": { "fontSize": 18, "fontWeight": "Bold" } } },
              { "type": "Text", "props": { "text": "Supporting copy goes here.", "style": { "fontSize": 14 } } }
            ]
          }
        ]
      }
    ]
  }
}
```

---

## A top app bar with back navigation

```json
{
  "screenId": "details",
  "schemaVersion": "1.0.0",
  "root": {
    "type": "Scaffold",
    "props": {
      "topBarTitle": "Details",
      "topBarNavigationIconUrl": "https://fonts.gstatic.com/s/i/materialiconsoutlined/arrow_back/v1/24px.svg"
    },
    "children": [
      {
        "type": "Column",
        "modifiers": [ { "type": "padding", "value": 16 } ],
        "children": [ { "type": "Text", "props": { "text": "Body content" } } ]
      }
    ]
  }
}
```

> The default scaffold navigation icon triggers a back/pop via the engine's navigation pipeline. If you
> need a custom action, wrap an `IconButton` in your layout with a `clickable` modifier.

---

## A header row with `weight` (title fills, icon hugs)

```json
{
  "type": "Row",
  "modifiers": [ { "type": "fillMaxWidth" }, { "type": "padding", "value": 8 } ],
  "props": { "verticalAlignment": "Center" },
  "children": [
    { "type": "Text", "props": { "text": "Inbox", "style": { "fontSize": 20, "fontWeight": "Bold" } },
      "modifiers": [ { "type": "weight", "value": 1.0 } ] },
    { "type": "IconButton", "props": { "url": "https://fonts.gstatic.com/s/i/materialiconsoutlined/search/v1/24px.svg" },
      "modifiers": [ { "type": "clickable", "value": { "type": "Navigate", "route": "search" } } ] }
  ]
}
```

---

## A form with two-way binding and submit

```json
{
  "screenId": "signup",
  "schemaVersion": "1.0.0",
  "title": "Sign up",
  "initialState": { "email": "", "password": "", "agree": false, "isLoading": false },
  "root": {
    "type": "Column",
    "modifiers": [ { "type": "fillMaxSize" }, { "type": "padding", "value": 24 } ],
    "props": { "space": 16 },
    "children": [
      { "type": "Text", "props": { "text": "Create your account", "style": { "fontSize": 24, "fontWeight": "Bold" } } },
      { "type": "TextField", "modifiers": [ { "type": "fillMaxWidth" } ],
        "props": { "stateKey": "email", "label": "Email", "keyboardType": "Email" } },
      { "type": "TextField", "modifiers": [ { "type": "fillMaxWidth" } ],
        "props": { "stateKey": "password", "label": "Password", "visualTransformation": "Password" } },
      { "type": "Checkbox", "props": { "stateKey": "agree", "label": "I agree to the terms" } },
      {
        "type": "Button",
        "modifiers": [
          { "type": "fillMaxWidth" },
          { "type": "clickable", "value": {
              "type": "Conditional",
              "stateKey": "agree", "operator": "eq", "value": true,
              "thenAction": {
                "type": "Sequence",
                "actions": [
                  { "type": "UpdateState", "stateKey": "isLoading", "value": true },
                  {
                    "type": "ApiCall",
                    "endpoint": "/api/signup", "method": "POST",
                    "body": { "email": "{{email}}", "password": "{{password}}" },
                    "onSuccess": { "type": "Navigate", "route": "welcome" },
                    "onError": { "type": "ShowSnackbar", "message": "Sign up failed" }
                  }
                ]
              },
              "elseAction": { "type": "ShowSnackbar", "message": "Please accept the terms first" }
          } }
        ],
        "props": { "text": "Create account", "variant": "filled" }
      }
    ]
  }
}
```

---

## Conditionally show a banner based on state

```json
{
  "type": "Conditional",
  "props": { "stateKey": "cartCount", "operator": "gt", "value": 0 },
  "children": [
    {
      "type": "Surface",
      "modifiers": [ { "type": "fillMaxWidth" }, { "type": "padding", "value": 12 } ],
      "props": { "color": "#FF4F378B", "shape": { "shape": "RoundedCorner", "radius": 12 } },
      "children": [
        { "type": "Text", "props": { "text": "{{cartCount}} items in your cart", "style": { "color": "#FFEADDFF" } } }
      ]
    }
  ]
}
```

---

## A gradient hero header

```json
{
  "type": "Box",
  "modifiers": [
    { "type": "fillMaxWidth" },
    { "type": "height", "value": 180 },
    { "type": "backgroundGradient", "value": { "colors": ["#FF1A0B2E", "#FF0F0D13"], "direction": "vertical" } }
  ],
  "props": { "contentAlignment": "Center" },
  "children": [
    { "type": "Text", "props": { "text": "Welcome back", "style": { "fontSize": 28, "fontWeight": "Bold", "color": "#FFFFFFFF" } } }
  ]
}
```

---

## A product grid

```json
{
  "screenId": "catalog",
  "schemaVersion": "1.0.0",
  "title": "Catalog",
  "root": {
    "type": "LazyGrid",
    "modifiers": [ { "type": "fillMaxSize" }, { "type": "padding", "value": 12 } ],
    "props": { "columns": 2, "space": 12 },
    "children": [
      {
        "type": "Card",
        "props": { "shape": { "shape": "RoundedCorner", "radius": 12 } },
        "modifiers": [ { "type": "clickable", "value": { "type": "Navigate", "route": "product", "params": { "id": "1" } } } ],
        "children": [
          {
            "type": "Column",
            "modifiers": [ { "type": "padding", "value": 8 } ],
            "props": { "space": 6 },
            "children": [
              { "type": "Image", "modifiers": [ { "type": "fillMaxWidth" }, { "type": "aspectRatio", "value": 1.0 } ],
                "props": { "url": "https://cdn.example.com/p1.jpg", "contentScale": "Crop" } },
              { "type": "Text", "props": { "text": "Product one", "style": { "fontWeight": "Medium" } } }
            ]
          }
        ]
      }
    ]
  }
}
```

---

## A confirmation dialog before a destructive action

```json
{
  "type": "Button",
  "props": { "text": "Delete account", "variant": "outlined" },
  "modifiers": [ { "type": "clickable", "value": {
    "type": "ShowDialog",
    "title": "Delete account?",
    "message": "This permanently removes your data.",
    "confirmText": "Delete",
    "dismissText": "Cancel",
    "onConfirm": {
      "type": "ApiCall", "endpoint": "/api/account/delete", "method": "POST",
      "onSuccess": { "type": "Navigate", "route": "goodbye" },
      "onError": { "type": "ShowSnackbar", "message": "Could not delete account" }
    }
  } } ]
}
```

---

## Greeting with template variables

```json
{
  "screenId": "home",
  "initialState": { "userName": "Alex", "unread": 3 },
  "schemaVersion": "1.0.0",
  "root": {
    "type": "Column",
    "modifiers": [ { "type": "padding", "value": 24 } ],
    "children": [
      { "type": "Text", "props": { "text": "Hi {{userName}}", "style": { "fontSize": 22, "fontWeight": "Bold" } } },
      { "type": "Text", "props": { "text": "You have {{unread}} unread messages" } }
    ]
  }
}
```

---

## Tips

- Keep deeply-nested trees under the 50-level cap (see [02 — JSON schema reference](./02-json-schema-reference.md)).
- Prefer `space` on `Column`/`Row` over manual `Spacer`s for consistent gaps.
- Use `weight` for proportional layouts inside rows/columns; it is ignored elsewhere.
- For host-specific UI (native checkout, maps, video), register a [custom component](./07-custom-components.md).
