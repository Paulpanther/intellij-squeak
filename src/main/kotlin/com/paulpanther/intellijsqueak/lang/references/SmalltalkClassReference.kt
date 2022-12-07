package com.paulpanther.intellijsqueak.lang.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import com.paulpanther.intellijsqueak.findChildrenOfType
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable
import com.paulpanther.intellijsqueak.services.squeak

class SmalltalkClassReference(
    element: SmalltalkVariable
): PsiReferenceBase<PsiElement>(element, element.textRange) {

    override fun resolve(): PsiElement? {
        val clazz = squeak.fileSystem.classWithName(element.text) ?: return null
        val file = PsiManager.getInstance(element.project).findFile(clazz) ?: return null
        return null
    }

    override fun getRangeInElement() = TextRange(0, element.textLength)
}
