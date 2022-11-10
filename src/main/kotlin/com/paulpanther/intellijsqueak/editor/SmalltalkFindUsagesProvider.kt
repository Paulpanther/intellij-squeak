package com.paulpanther.intellijsqueak.editor

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.TokenSet
import com.paulpanther.intellijsqueak.lang.definition.SmalltalkLexerAdapter
import com.paulpanther.intellijsqueak.lang.definition.SmalltalkTokenSets
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable

class SmalltalkFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner() = DefaultWordsScanner(
        SmalltalkLexerAdapter(),
        SmalltalkTokenSets.identifier,
        SmalltalkTokenSets.comment,
        TokenSet.EMPTY)

    override fun canFindUsagesFor(psiElement: PsiElement) =
        psiElement is PsiNamedElement

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        return when (element) {
            is SmalltalkVariable -> "variable"
            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return when (element) {
            is SmalltalkVariable -> element.text
            else -> ""
        }
    }

    override fun getNodeText(
        element: PsiElement,
        useFullName: Boolean
    ): String {
        return getDescriptiveName(element)
    }
}
