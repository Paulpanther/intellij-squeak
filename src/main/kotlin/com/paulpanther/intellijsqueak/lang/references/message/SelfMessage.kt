package com.paulpanther.intellijsqueak.lang.references.message

import com.intellij.psi.PsiElement
import com.paulpanther.intellijsqueak.lang.SmalltalkExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkLiteralExpr
import com.paulpanther.intellijsqueak.lang.util.classFile

class SelfMessage(
    private val expression: SmalltalkExpr,
    private val identifier: PsiElement
): MessageReferenceHelper {
    private val isSelf get() = (expression is SmalltalkLiteralExpr) && expression.text == "self"

    /** TODO use instance when current file is instance, else class */
    private val methodsInSelf get() = expression.classFile?.instanceMethods

    override fun resolve(): PsiElement? {
        if (!isSelf) return null
        return methodsInSelf
            ?.find { it.name == identifier.text }
            ?.findPsiMethod(identifier.project)
    }

    override fun getVariants(): List<String>? {
        if (!isSelf) return null
        return methodsInSelf?.map { it.name }
    }
}
