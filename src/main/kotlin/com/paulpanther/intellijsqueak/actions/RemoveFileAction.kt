package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFile

class RemoveFileAction(
    childName: String,
    parentName: String?,
    private val file: SmalltalkVirtualFile
): AnAction(
    "Remove $childName",
    "Remove a $childName${parentName?.let { "for the selected $parentName" } ?: "" }",
    file.icon()
) {
    override fun actionPerformed(e: AnActionEvent) {
        file.delete(null)
    }
}
