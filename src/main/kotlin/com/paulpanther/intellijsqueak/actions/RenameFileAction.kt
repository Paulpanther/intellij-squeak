package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.paulpanther.intellijsqueak.ui.dialog.RenameFileDialog
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFile

class RenameFileAction(
    private val fileName: String,
    private val file: SmalltalkVirtualFile,
): AnAction(
    "Rename $fileName",
    "Rename a $fileName",
    file.icon()
) {
    override fun actionPerformed(e: AnActionEvent) {
        RenameFileDialog(e.project ?: return, fileName, file).show()
    }
}
