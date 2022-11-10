package com.paulpanther.intellijsqueak.editor

import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class SmalltalkSyntaxHighlighterFactory: SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(
        project: Project?,
        virtualFile: VirtualFile?
    ) = SmalltalkSyntaxHighlighter()
}
