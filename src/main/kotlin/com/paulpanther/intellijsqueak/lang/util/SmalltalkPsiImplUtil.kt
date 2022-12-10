package com.paulpanther.intellijsqueak.lang.util

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.parentOfType
import com.intellij.testFramework.LightVirtualFile
import com.paulpanther.intellijsqueak.lang.SmalltalkBlockArgument
import com.paulpanther.intellijsqueak.lang.SmalltalkExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkNamedIdentifier
import com.paulpanther.intellijsqueak.lang.SmalltalkUnaryMessageExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkUnaryMessageIdentifier
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable
import com.paulpanther.intellijsqueak.lang.references.SmalltalkArgumentVariableReference
import com.paulpanther.intellijsqueak.lang.references.SmalltalkClassReference
import com.paulpanther.intellijsqueak.lang.references.SmalltalkMessageReference
import com.paulpanther.intellijsqueak.lang.references.SmalltalkTemporaryVariableReference
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileMethod

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
    fun getReferences(variable: PsiElement): Array<PsiReferenceBase<PsiElement>> {
        return when(variable) {
            is SmalltalkVariable -> getVariablesReferences(variable)
            is SmalltalkUnaryMessageIdentifier -> getUnaryMessageReferences(variable)
            else -> arrayOf()
        }
    }

    private fun getVariablesReferences(variable: SmalltalkVariable): Array<PsiReferenceBase<PsiElement>> {
        return arrayOf(
            SmalltalkTemporaryVariableReference(variable),
            SmalltalkClassReference(variable),
            SmalltalkArgumentVariableReference(variable))
    }


    private fun getUnaryMessageReferences(identifier: SmalltalkUnaryMessageIdentifier): Array<PsiReferenceBase<PsiElement>> {
        val message = identifier.parent as SmalltalkUnaryMessageExpr
        return arrayOf(SmalltalkMessageReference(message.expr, identifier.identifier))
    }
}


val SmalltalkExpr.methodFile get(): SmalltalkVirtualFileMethod? {
    val file = containingFile?.virtualFile
    return (if (file is LightVirtualFile) file.originalFile else file) as? SmalltalkVirtualFileMethod
}

val SmalltalkExpr.classFile get() = methodFile?.clazz
