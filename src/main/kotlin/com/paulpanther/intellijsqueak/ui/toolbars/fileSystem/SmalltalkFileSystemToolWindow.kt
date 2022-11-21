package com.paulpanther.intellijsqueak.ui.toolbars.fileSystem

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.paulpanther.intellijsqueak.services.squeak

class SmalltalkFileSystemToolWindow : ToolWindowFactory {
    override fun createToolWindowContent(
        project: Project,
        toolWindow: ToolWindow
    ) {
        squeak.state.onEnabledChanged {
            toolWindow.isAvailable = it
        }

        val manager = toolWindow.contentManager
        val content = manager.factory.createContent(
            SmalltalkFileSystemView(project),
            null,
            false
        ).apply { isCloseable = false }
        manager.addContent(content)
    }

    override fun isApplicable(project: Project) = squeak.state.isEnabled
}
