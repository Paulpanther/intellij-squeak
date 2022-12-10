package com.paulpanther.intellijsqueak.lang.references.message

import com.intellij.psi.PsiElement
import com.paulpanther.intellijsqueak.lang.SmalltalkExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkReferenceExpr
import com.paulpanther.intellijsqueak.lang.psi.SmalltalkClass
import com.paulpanther.intellijsqueak.lang.references.SmalltalkClassReference
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass

class ClassMessage(
    private val expression: SmalltalkExpr,
    private val identifier: PsiElement
): MessageReferenceHelper {
    fun getReferencedClass(): SmalltalkVirtualFileClass? {
        val variable = (expression as? SmalltalkReferenceExpr)?.variable ?: return null
        val toClass = SmalltalkClassReference(variable).resolve() as? SmalltalkClass ?: return null
        return toClass.classFile
    }

    override fun resolve(): PsiElement? {
        return getReferencedClass()?.classMethods
            ?.find { it.name == identifier.text }
            ?.findPsiMethod(identifier.project)
    }

    override fun getVariants(): List<String>? {
        return getReferencedClass()?.classMethods?.map { it.name }
    }
}
