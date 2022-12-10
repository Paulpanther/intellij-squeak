package com.paulpanther.intellijsqueak.lang.references

import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.findParentOfType
import com.paulpanther.intellijsqueak.lang.SmalltalkMethod
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable

/**
 * TODO maybe override getUseScope?
 */
class SmalltalkTemporaryVariableReference(
    element: SmalltalkVariable,
): PsiReferenceBase<PsiElement>(element, element.textRange) {

    override fun resolve(): PsiElement? {
        val method =
            element.findParentOfType<SmalltalkMethod>(true) ?: return null
        val temporaries = method.temporaries?.temporaryList ?: return null
        return temporaries.find { it.text == element.text }
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
        val temporaries = method.temporaries?.temporaryList ?: return arrayOf()

        return temporaries.map {
            LookupElementBuilder
                .create(it.text)
                .withAutoCompletionPolicy(AutoCompletionPolicy.SETTINGS_DEPENDENT)
        }.toTypedArray()
    }
}
