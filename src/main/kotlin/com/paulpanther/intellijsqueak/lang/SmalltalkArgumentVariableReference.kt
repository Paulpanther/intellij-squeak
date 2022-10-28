package com.paulpanther.intellijsqueak.lang

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.findParentOfType

/**
 * TODO maybe override getUseScope?
 */
class SmalltalkArgumentVariableReference(
    element: SmalltalkVariable,
): PsiReferenceBase<PsiElement>(element, element.textRange) {

    override fun resolve(): PsiElement? {
        val method =
            element.findParentOfType<SmalltalkMethod>(true) ?: return null
        val arguments = method.keywordSelector?.argumentList ?: return null
        return arguments.find { it.text == element.text }
    }

    override fun getRangeInElement(): TextRange {
        return TextRange(0, element.textLength)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        val node = (element as SmalltalkVariable).identifier.node as LeafPsiElement
        node.replaceWithText(newElementName)
        return element
    }

    override fun getVariants(): Array<Any> {
        val method = element.findParentOfType<SmalltalkMethod>(true) ?: return arrayOf()
        val arguments = method.keywordSelector?.argumentList ?: return arrayOf()

        return arguments.map {
            LookupElementBuilder.create(it)
        }.toTypedArray()
    }
}
