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
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JPanel

class AgentAssistToolWindowFactory : ToolWindowFactory, DumbAware {
    private data class Assistant(val name: String, val url: String)

    private val assistants = listOf(
        Assistant("Copilot", "https://github.com/features/copilot"),
        Assistant("Kiro", "https://kiro.dev"),
        Assistant("Claude", "https://claude.ai")
    )

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val rootPanel = JPanel(BorderLayout())
        if (!JBCefApp.isSupported()) {
            rootPanel.add(
                JBLabel("Embedded browser is not supported in this IDE/JDK. Please open assistant websites externally."),
                BorderLayout.NORTH
            )
            rootPanel.add(createExternalLinksPanel(), BorderLayout.CENTER)
        } else {
            val tabs = JBTabbedPane()
            assistants.forEach { assistant ->
                tabs.addTab(assistant.name, createAssistantTab(assistant.url))
            }
            rootPanel.add(tabs, BorderLayout.CENTER)
        }

        val content = ContentFactory.getInstance().createContent(rootPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun createAssistantTab(url: String): JPanel {
        val browser = JBCefBrowser(url)
        val panel = JPanel(BorderLayout())
        val controls = JPanel(FlowLayout(FlowLayout.LEFT))

        val reloadButton = JButton("Reload").apply {
            addActionListener { browser.cefBrowser.reload() }
        }
        val openExternalButton = JButton("Open External").apply {
            addActionListener { BrowserUtil.browse(url) }
        }

        controls.add(reloadButton)
        controls.add(openExternalButton)
        panel.add(controls, BorderLayout.NORTH)
        panel.add(browser.component, BorderLayout.CENTER)

        return panel
    }

    private fun createExternalLinksPanel(): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))
        assistants.forEach { assistant ->
            val button = JButton("Open ${assistant.name}").apply {
                addActionListener { BrowserUtil.browse(assistant.url) }
            }
            panel.add(button)
        }
        return panel
    }
}
