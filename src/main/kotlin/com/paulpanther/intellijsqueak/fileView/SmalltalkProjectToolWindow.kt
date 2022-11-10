package com.paulpanther.intellijsqueak.fileView

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class SmalltalkProjectToolWindow: ToolWindowFactory {
    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow
    ) {
        val manager = toolWindow.contentManager
        val content = manager.factory.createContent(
            SmalltalkFileSystemView(project, true),
            null,
            false
        ).apply { isCloseable = false }
        manager.addContent(content)
    }
}
