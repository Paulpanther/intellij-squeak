package com.paulpanther.intellijsqueak

import com.intellij.codeInsight.daemon.RainbowVisitor
import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.paulpanther.intellijsqueak.lang.SmalltalkFile
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable
import com.paulpanther.intellijsqueak.lang.isArgument
import com.paulpanther.intellijsqueak.lang.isTemporary

/** TODO isn't called for some reason */
class SmalltalkSemanticHighlighter: RainbowVisitor() {
    override fun suitableForFile(file: PsiFile) =
        file is SmalltalkFile

    override fun visit(element: PsiElement) {
        if (element is SmalltalkVariable) {
            if (element.isTemporary()) {
                highlight(element, SmalltalkSyntaxHighlighter.temporaryVariable)
            } else if (element.isArgument()) {
                highlight(element, SmalltalkSyntaxHighlighter.argumentVariable)
            }
        }
    }

    private fun highlight(element: PsiElement, key: TextAttributesKey) {
        addInfo(getInfo(element.containingFile, element, element.text, key))
    }

    override fun clone() = SmalltalkSemanticHighlighter()
}
