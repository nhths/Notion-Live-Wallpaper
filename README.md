# Simple Notion task list as live wallpaper

## How to Build
 - add keys.gradle in ./app/
 - paste this:
```
import groovy.transform.MapConstructor

@MapConstructor
class Field {
    String type, name, value
}

ext {
    keysDebug = [
            new Field (type: "String", name: "OAuthClientID", value: '"null"'),
            new Field (type: "String", name: "OAuthClientSecret", value: '"null"'),
            new Field (type: "String", name: "OAuthRedirectUri", value: '"null"')
    ]

    keysRelease = keysDebug
}
```
If you need builtin keys:
 - follow link: https://www.notion.so/my-integrations
 - add Public integration
 - switch null values to integration keys
*IMPORTANT*
Use double quotes!!! ```Example: new Field (type: "String", name: "OAuthClientID", value: '"379b996e-fd9f-457e-a2cb-b26f1948de38"')```