package com.paulpanther.intellijsqueak.lang.util

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.paulpanther.intellijsqueak.lang.SmalltalkBlockArgument
import com.paulpanther.intellijsqueak.lang.SmalltalkNamedIdentifier
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable
import com.paulpanther.intellijsqueak.lang.references.SmalltalkArgumentVariableReference
import com.paulpanther.intellijsqueak.lang.references.SmalltalkTemporaryVariableReference

object SmalltalkPsiImplUtil {
    @JvmStatic
    fun getName(identifier: SmalltalkNamedIdentifier): String = identifier.text

    @JvmStatic
    fun getName(identifier: SmalltalkBlockArgument): String = identifier.text

    @JvmStatic
    fun setName(identifier: SmalltalkNamedIdentifier, newName: String): PsiElement? {
        val node = identifier.identifier.node as? LeafPsiElement ?: return null
        node.replaceWithText(newName)
        return identifier
    }

    @JvmStatic
    fun setName(identifier: SmalltalkBlockArgument, newName: String): PsiElement? {
        val node = identifier.blockArgumentIdentifier.node as? LeafPsiElement ?: return null
        node.replaceWithText(newName)
        return identifier
    }

    @JvmStatic
    fun getNameIdentifier(identifier: SmalltalkNamedIdentifier): PsiElement? {
        return identifier.identifier
    }

    @JvmStatic
    fun getNameIdentifier(identifier: SmalltalkBlockArgument): PsiElement? {
        return identifier.blockArgumentIdentifier
    }

    @JvmStatic
    fun getReferences(variable: SmalltalkVariable): Array<PsiReferenceBase<PsiElement>> {
        return arrayOf(
            SmalltalkTemporaryVariableReference(variable),
            SmalltalkArgumentVariableReference(variable),
        )
    }
}
