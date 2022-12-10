package com.paulpanther.intellijsqueak.lang.references.message

import com.intellij.psi.PsiElement
import com.paulpanther.intellijsqueak.lang.SmalltalkExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkReferenceExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkUnaryMessageExpr
import com.paulpanther.intellijsqueak.lang.psi.SmalltalkClass
import com.paulpanther.intellijsqueak.lang.references.SmalltalkClassReference
import com.paulpanther.intellijsqueak.lang.references.SmalltalkMessageReference
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass

class InstanceMessage(
    private val expression: SmalltalkExpr,
    private val identifier: PsiElement
): MessageReferenceHelper {
    private fun getReferencedClass(): SmalltalkVirtualFileClass? {
        val message = expression as? SmalltalkUnaryMessageExpr ?: return null
        if (message.unaryMessageIdentifier.identifier.text != "new") return null

        return ClassMessage(message.expr, message.unaryMessageIdentifier.identifier).getReferencedClass()
    }

    override fun resolve(): PsiElement? {
        return getReferencedClass()?.instanceMethods
            ?.find { it.name == identifier.text }
            ?.findPsiMethod(identifier.project)
    }

    override fun getVariants(): List<String>? {
        return getReferencedClass()?.classMethods?.map { it.name }
    }
}
