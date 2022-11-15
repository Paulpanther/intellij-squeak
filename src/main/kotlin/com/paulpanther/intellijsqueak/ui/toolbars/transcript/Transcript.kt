package com.paulpanther.intellijsqueak.ui.toolbars.transcript

import com.intellij.execution.impl.ConsoleViewUtil
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.util.application
import com.paulpanther.intellijsqueak.services.squeak

class TranscriptFactory: ToolWindowFactory {
    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow
    ) {
        val console = ConsoleViewUtil.setupConsoleEditor(project, false, false)
        Disposer.register(toolWindow.disposable) {
            EditorFactory.getInstance().releaseEditor(console)
        }

        application.runReadAction {
            console.settings.isBlinkCaret = false
        }

        squeak.onTranscriptChange {
            console.document.insertString(console.document.textLength, it)
        }

        val manager = toolWindow.contentManager
        val content = manager.factory.createContent(
            console.component,
            null,
            false
        ).apply { isCloseable = false }
        manager.addContent(content)
    }
}
