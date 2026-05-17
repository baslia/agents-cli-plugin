# AI CLIs (PyCharm Plugin)

This plugin adds an **AI CLIs** tool window on the right side of PyCharm with tabs for:
- copilot
- kiro-cli
- claude-code

Each tab opens a dedicated embedded bash terminal and runs the matching CLI command:
- `copilot`
- `kiro-cli`
- `claude-code`

If a command is not available, the tab falls back to the assistant website in-app (or to an external browser button when JCEF is unavailable).

## Run in Development

Use Gradle:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew runIde
```

## Test the Plugin in Sandbox IDE

1. Start the sandbox IDE with `./gradlew runIde`.
2. In the sandbox PyCharm, open any project.
3. Open **View -> Tool Windows -> AI CLIs** (or use the right sidebar icon).
4. Verify the tool window renders and tabs are present:
   - copilot
   - kiro-cli
   - claude-code
5. Smoke-test interactions:
   - Open/close the tool window
   - Switch tabs
   - Verify a bash terminal appears in each tab
   - Verify the CLI command auto-runs in that terminal
   - If CLI is missing, verify website fallback loads
6. Restart the sandbox IDE and confirm the plugin still loads.

## Build Plugin ZIP

```bash
./gradlew buildPlugin
```

Install the ZIP from `build/distributions/` in a regular PyCharm instance:

1. Open **Settings -> Plugins**
2. Click the gear icon
3. Choose **Install Plugin from Disk**
4. Select the generated ZIP

## Versioned GitHub Releases

The repository is configured to publish a GitHub Release with the plugin ZIP when you push a semantic version tag:

```bash
git tag v0.2.0
git push origin v0.2.0
```

The workflow will:
1. Build with `-PpluginVersion=0.2.0`
2. Produce `build/distributions/*.zip`
3. Create a GitHub Release `v0.2.0` and attach the ZIP

You can also trigger the workflow manually from **Actions -> Release plugin** and provide a `version` input.

## Publish to JetBrains Marketplace

1. Build a versioned ZIP:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew -PpluginVersion=0.2.0 buildPlugin
```

2. Manual upload:
   - Go to https://plugins.jetbrains.com
   - Open your plugin in **My Plugins**
   - Upload the ZIP from `build/distributions/`
   - Publish the new version

3. Optional CLI publish with token:

```bash
export ORG_GRADLE_PROJECT_intellijPublishToken=YOUR_MARKETPLACE_TOKEN
./gradlew -PpluginVersion=0.2.0 publishPlugin
```
