package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.PopupHandler
import com.paulpanther.intellijsqueak.vfs.*
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
            is SmalltalkVirtualFilePackage -> arrayOf(
                NewFileAction("Class", "Package", file),
                NewFileAction("Package", null, file.root),
                RefreshFileAction("Package", file),
                RemoveFileAction("Package", null, file))
            is SmalltalkVirtualFileClass -> arrayOf(
                NewFileAction("Category", "Class", file),
                RefreshFileAction("Class", file),
                RemoveFileAction("Class", "Package", file))
            is SmalltalkVirtualFileCategory -> arrayOf(
                NewFileAction("Method", "Category", file),
                RefreshFileAction("Category", file),
                RemoveFileAction("Category", "Class", file))
            is SmalltalkVirtualFileMethod -> arrayOf(
                NewFileAction("Method", "Category", file.category),
                RefreshFileAction("Method", file),
                RemoveFileAction("Method", "Category", file))
            else -> arrayOf()
        }
    }
}
