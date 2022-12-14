package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFile

class RemoveFileAction(
    childName: String,
    private val file: SmalltalkVirtualFile
): AnAction(
    "Remove $childName",
    "Remove a $childName",
    file.icon()
) {
    override fun actionPerformed(e: AnActionEvent) {
        file.delete(null)
    }
}
