package com.paulpanther.intellijsqueak.ui.toolbars.transcript

import com.intellij.execution.impl.ConsoleViewUtil
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.application
import com.intellij.util.ui.AbstractLayoutManager
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.wsClient.SqueakClientStateListener
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Container
import java.awt.Dimension
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class TranscriptFactory: ToolWindowFactory {
    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow
    ) {
        val transcript = Transcript(toolWindow)

        val manager = toolWindow.contentManager
        val content = manager.factory.createContent(
            transcript,
            null,
            false
        ).apply { isCloseable = false }
        manager.addContent(content)
    }
}

class Transcript(toolWindow: ToolWindow): SimpleToolWindowPanel(false, true), Disposable, SqueakClientStateListener {
    private val console: EditorEx

    init {
        Disposer.register(toolWindow.disposable, this)

        console = ConsoleViewUtil.setupConsoleEditor(toolWindow.project, false, false)
        application.runReadAction {
            console.settings.isBlinkCaret = false
            console.settings.isWhitespacesShown = false
        }

        // Copied from EventLogToolWindowFactory
        val editorPanel = object: JPanel(object: AbstractLayoutManager() {
            private fun getOffset(): Int {
                return JBUIScale.scale(4)
            }

            override fun preferredLayoutSize(parent: Container): Dimension {
                val size = parent.getComponent(0).preferredSize
                return Dimension(size.width + getOffset(), size.height)
            }

            override fun layoutContainer(parent: Container) {
                val offset = getOffset()
                parent.getComponent(0).setBounds(offset, 0, parent.width - offset, parent.height)
            }
        }) {
            override fun getBackground(): Color {
                return console.backgroundColor
            }
        }
        editorPanel.add(console.component)

        squeak.onTranscriptChange {
            console.document.insertString(console.document.textLength, it + "\n")
        }

        val toolbar = createToolbar()
        toolbar.targetComponent = console.contentComponent
        setToolbar(toolbar.component)
        setContent(editorPanel)
    }

    private fun createToolbar(): ActionToolbar {
        val group = DefaultActionGroup()
//        group.add(ActionManager.getInstance().getAction(IdeActions.CONSOLE_CLEAR_ALL));
//        group.add(new EventLogConsole.ClearLogAction(console));
//        group.addSeparator();
//        group.add(new EditNotificationSettings(project));

        return ActionManager.getInstance().createActionToolbar("Transcript", group, false)
    }

    override fun onOpen() {}

    override fun onClose() {}

    override fun dispose() {
        EditorFactory.getInstance().releaseEditor(console)
    }
}
