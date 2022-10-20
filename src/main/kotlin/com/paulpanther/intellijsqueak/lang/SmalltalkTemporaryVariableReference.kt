package com.paulpanther.intellijsqueak.lang

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType

class SmalltalkTemporaryVariableReference(
    element: SmalltalkVariable,
): PsiReferenceBase<PsiElement>(element, element.textRange) {

    override fun resolve(): PsiElement? {
        val method =
            element.getParentOfType<SmalltalkMethod>(true) ?: return null
        val temporaries = method.temporaries?.namedIdentifierList ?: return null
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
}
