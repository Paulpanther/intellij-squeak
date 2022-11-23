package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.PopupHandler
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFile
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileCategory
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileMethod
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileSystem
import javax.swing.JTree

class FileSystemGroup(
    private val system: SmalltalkVirtualFileSystem,
    private val tree: JTree,
): ActionGroup() {
    init {
        PopupHandler.installPopupMenu(tree, this, "popup@SmalltalkFileSystemView")
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val path = tree.selectionPath ?: return arrayOf()
        val file = system.findFileByPath(path.path.map { it.toString() })

        return when (file) {
            is SmalltalkVirtualFileCategory -> arrayOf(NewMethodAction(file))
            else -> arrayOf()
        }
    }
}
