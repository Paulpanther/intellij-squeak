package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.util.OpenSourceUtil
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass

class OpenClassAction(
    val file: SmalltalkVirtualFileClass
): AnAction("Open Class", "Edit properties and comment", SmalltalkIcons.clazz) {
    override fun actionPerformed(e: AnActionEvent) {
        OpenSourceUtil.navigate(OpenFileDescriptor(e.project ?: return, file))
    }
}
