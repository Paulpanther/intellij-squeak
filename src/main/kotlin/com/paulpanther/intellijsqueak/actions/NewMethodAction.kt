package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.paulpanther.intellijsqueak.ui.dialog.AddMethodDialog
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileCategory

class NewMethodAction(private val categoryNode: SmalltalkVirtualFileCategory): AnAction(
    "New Method",
    "Create a new method for the selected Class",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        AddMethodDialog(e.project ?: return, categoryNode).show()
    }
}
