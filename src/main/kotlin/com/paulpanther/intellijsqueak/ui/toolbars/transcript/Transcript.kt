package com.paulpanther.intellijsqueak.ui.toolbars.transcript

import com.intellij.execution.console.BaseConsoleExecuteActionHandler
import com.intellij.execution.console.LanguageConsoleBuilder
import com.intellij.execution.console.LanguageConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.AbstractLayoutManager
import com.paulpanther.intellijsqueak.lang.definition.SMALLTALK_CONSOLE_KEY
import com.paulpanther.intellijsqueak.lang.definition.SmalltalkLanguage
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.wsClient.SqueakClientStateListener
import java.awt.Color
import java.awt.Container
import java.awt.Dimension
import javax.swing.JPanel

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

class Transcript(toolWindow: ToolWindow)
    : SimpleToolWindowPanel(false, true), Disposable, SqueakClientStateListener {
    private val console: LanguageConsoleView

    init {
        Disposer.register(toolWindow.disposable, this)

        val executeHandler = object: BaseConsoleExecuteActionHandler(true) {
            override fun execute(text: String, console: LanguageConsoleView) {
                this@Transcript.execute(text, console)
            }
        }

        console = LanguageConsoleBuilder()
            .oneLineInput()
            .initActions(executeHandler, "st")
            .build(toolWindow.project, SmalltalkLanguage)
        console.virtualFile.putUserData(SMALLTALK_CONSOLE_KEY, true)

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
                return console.component.background
            }
        }
        editorPanel.add(console.component)

        squeak.onTranscriptChange {
            console.print(it + "\n", ConsoleViewContentType.SYSTEM_OUTPUT)
        }

        val toolbar = createToolbar()
        toolbar.targetComponent = console.component
        setToolbar(toolbar.component)
        setContent(editorPanel)
    }

    private fun execute(text: String, console: LanguageConsoleView)  {
        squeak.evaluate(text) {
            console.print(it + "\n", ConsoleViewContentType.NORMAL_OUTPUT)
        }
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
        console.dispose()
    }
}
