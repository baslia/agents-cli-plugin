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
