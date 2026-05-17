package com.adelbasli.aiagents

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.terminal.ui.TerminalWidget
import org.jetbrains.plugins.terminal.TerminalToolWindowManager
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JPanel

class AgentAssistToolWindowFactory : ToolWindowFactory, DumbAware {
    private data class Assistant(val name: String, val command: String, val url: String)

    private val assistants = listOf(
        Assistant("copilot", "copilot", "https://github.com/features/copilot"),
        Assistant("kiro-cli", "kiro-cli", "https://kiro.dev"),
        Assistant("claude-code", "claude-code", "https://claude.ai")
    )

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val rootPanel = JPanel(BorderLayout())
        val tabs = JBTabbedPane()
        assistants.forEach { assistant ->
            tabs.addTab(assistant.name, createAssistantTab(project, assistant))
        }
        rootPanel.add(tabs, BorderLayout.CENTER)

        val content = ContentFactory.getInstance().createContent(rootPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun createAssistantTab(project: Project, assistant: Assistant): JPanel {
        if (!commandExists(assistant.command)) {
            return createWebsiteFallbackTab(
                assistant,
                "Command '${assistant.command}' was not found in PATH. Showing website fallback."
            )
        }

        return createTerminalTab(project, assistant)
    }

    private fun createTerminalTab(project: Project, assistant: Assistant): JPanel {
        val panel = JPanel(BorderLayout())
        val projectBasePath = projectPathOrHome(project)
        val toolWindowManager = TerminalToolWindowManager.getInstance(project)
        val terminalWidget = toolWindowManager.createShellWidget(projectBasePath, assistant.name, false, false)

        runCliCommand(terminalWidget, assistant.command)
        panel.add(terminalWidget.component, BorderLayout.CENTER)
        return panel
    }

    private fun createWebsiteFallbackTab(assistant: Assistant, message: String): JPanel {
        if (!JBCefApp.isSupported()) {
            return JPanel(BorderLayout()).apply {
                add(JBLabel(message), BorderLayout.NORTH)
                add(
                    JButton("Open ${assistant.name} website").apply {
                        addActionListener { BrowserUtil.browse(assistant.url) }
                    },
                    BorderLayout.CENTER
                )
            }
        }

        val browser = JBCefBrowser(assistant.url)
        val panel = JPanel(BorderLayout())
        val controls = JPanel(FlowLayout(FlowLayout.LEFT))
        controls.add(JButton("Reload").apply { addActionListener { browser.cefBrowser.reload() } })
        controls.add(JButton("Open External").apply { addActionListener { BrowserUtil.browse(assistant.url) } })
        panel.add(controls, BorderLayout.NORTH)
        panel.add(browser.component, BorderLayout.CENTER)
        panel.add(JBLabel(message), BorderLayout.SOUTH)
        return panel
    }

    private fun runCliCommand(terminalWidget: TerminalWidget, command: String) {
        terminalWidget.sendCommandToExecute(command)
    }

    private fun commandExists(command: String): Boolean {
        val checker = ProcessBuilder("bash", "-lc", "command -v $command >/dev/null 2>&1")
            .redirectErrorStream(true)
            .start()
        return checker.waitFor() == 0
    }

    private fun projectPathOrHome(project: Project?): String =
        project?.basePath ?: System.getProperty("user.home")
}
