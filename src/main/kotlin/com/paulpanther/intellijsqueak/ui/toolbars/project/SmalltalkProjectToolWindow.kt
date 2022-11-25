package com.paulpanther.intellijsqueak.ui.toolbars.project

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.paulpanther.intellijsqueak.ui.toolbars.fileSystem.SmalltalkFileSystemView

class SmalltalkProjectToolWindow: ToolWindowFactory {
    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow
    ) {
        val manager = toolWindow.contentManager
        val content = manager.factory.createContent(
            SmalltalkFileSystemView(project),
            null,
            false
        ).apply { isCloseable = false }
        manager.addContent(content)
    }
}
