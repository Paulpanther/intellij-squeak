package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.paulpanther.intellijsqueak.ui.dialog.AddMethodDialog
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileCategory
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileMethod

class RemoveMethodAction(private val method: SmalltalkVirtualFileMethod): AnAction(
    "Remove Method",
    "Remove a method for the selected category",
    null
) {
    override fun actionPerformed(e: AnActionEvent) {
        method.delete(null)
    }
}
