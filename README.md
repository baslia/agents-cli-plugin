# AI Agents Panel (PyCharm Plugin)

This plugin adds an **AI Agents** tool window on the right side of PyCharm with tabs for:
- Copilot
- Kiro
- Claude

Each tab opens the assistant website inside the IDE via JCEF, with:
- **Reload** button
- **Open External** button

If JCEF is unavailable, the panel shows buttons to open each assistant in your default browser.

## Run in Development

Use Gradle:

```bash
./gradlew runIde
```

## Build Plugin ZIP

```bash
./gradlew buildPlugin
```
