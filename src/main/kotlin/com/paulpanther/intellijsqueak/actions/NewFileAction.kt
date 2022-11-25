package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.paulpanther.intellijsqueak.ui.dialog.AddFileDialog
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileDirectory
import com.paulpanther.intellijsqueak.vfs.childIcon

class NewFileAction(
    private val childName: String,
    private val parentName: String?,
    private val parent: SmalltalkVirtualFileDirectory<*>,
): AnAction(
    "New $childName",
    "Create a new $childName${parentName?.let { " for the selected $parentName" } ?: ""}",
    parent.childIcon
) {
    override fun actionPerformed(e: AnActionEvent) {
        AddFileDialog(e.project ?: return, childName, parentName, parent).show()
    }
}
