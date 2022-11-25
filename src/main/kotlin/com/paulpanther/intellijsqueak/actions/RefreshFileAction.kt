package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFile

class RefreshFileAction(
    childName: String,
    private val file: SmalltalkVirtualFile
): AnAction(
    "Refresh $childName",
    "Refresh a $childName",
    file.icon()
) {
    override fun actionPerformed(e: AnActionEvent) {
        file.refresh(true, true)
    }
}
