package com.paulpanther.intellijsqueak.lang.references

import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable
import com.paulpanther.intellijsqueak.lang.psi.SmalltalkClass
import com.paulpanther.intellijsqueak.services.squeak

class SmalltalkClassReference(
    element: SmalltalkVariable
): PsiReferenceBase<PsiElement>(element, element.textRange) {

    override fun resolve(): PsiElement? {
        val clazz = squeak.fileSystem.classWithName(element.text) ?: return null
        return SmalltalkClass(element.project, clazz)
    }

    override fun getRangeInElement() = TextRange(0, element.textLength)

    override fun getVariants(): Array<Any> {
        val classNames = squeak.fileSystem.classes.map { it.name }

        return classNames.map {
            LookupElementBuilder
                .create(it)
                .withAutoCompletionPolicy(AutoCompletionPolicy.SETTINGS_DEPENDENT)
        }.toTypedArray()
    }
}
