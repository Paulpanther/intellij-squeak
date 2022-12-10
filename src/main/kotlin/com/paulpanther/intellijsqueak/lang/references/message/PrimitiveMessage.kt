package com.paulpanther.intellijsqueak.lang.references.message

import com.intellij.psi.PsiElement
import com.paulpanther.intellijsqueak.lang.SmalltalkArrayExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkBlockExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkLiteralExpr
import com.paulpanther.intellijsqueak.lang.SmalltalkSymbolLiteral
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass

class PrimitiveMessage(
    private val expression: SmalltalkExpr,
    private val identifier: PsiElement
) : MessageReferenceHelper {

    override fun resolve(): PsiElement? {
        val clazz = getReferencedClass()
        return clazz?.instanceMethods
            ?.find { it.name == identifier.text }
            ?.findPsiMethod(identifier.project)
    }

    private fun getReferencedClass(): SmalltalkVirtualFileClass? {
        val className = getClassName() ?: return null
        return squeak.fileSystem.classWithName(className)
    }

    private fun getClassName() = when (expression) {
        is SmalltalkLiteralExpr -> when {
            expression is SmalltalkSymbolLiteral -> "ByteSymbol"
            expression.string != null -> "ByteString"
            expression.number != null -> "Integer"
            expression.character != null -> "Character"
            expression.text == "nil" -> "UndefinedObject"
            expression.text == "true" -> "True"
            expression.text == "false" -> "False"
            else -> null
        }

        is SmalltalkBlockExpr -> "BlockClosure"
        is SmalltalkArrayExpr -> "Array"
        else -> null
    }

    override fun getVariants(): List<String>? {
        val clazz = getReferencedClass()
        return clazz?.instanceMethods?.map { it.name }
    }
}
