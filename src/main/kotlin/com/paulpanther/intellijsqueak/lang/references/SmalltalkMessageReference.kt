package com.paulpanther.intellijsqueak.lang.references

import com.intellij.codeInsight.lookup.AutoCompletionPolicy
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.paulpanther.intellijsqueak.lang.SmalltalkExpr
import com.paulpanther.intellijsqueak.lang.references.message.ClassMessage
import com.paulpanther.intellijsqueak.lang.references.message.InstanceMessage
import com.paulpanther.intellijsqueak.lang.references.message.PrimitiveMessage
import com.paulpanther.intellijsqueak.lang.references.message.SelfMessage


class SmalltalkMessageReference(
    expression: SmalltalkExpr,
    identifier: PsiElement
): PsiReferenceBase<PsiElement>(identifier, identifier.textRange) {
    private val resolvers = listOf(
        SelfMessage(expression, identifier),
        ClassMessage(expression, identifier),
        InstanceMessage(expression, identifier),
        PrimitiveMessage(expression, identifier),
    )

    override fun resolve(): PsiElement? {
        return resolvers.firstNotNullOfOrNull { it.resolve() }
    }

    override fun getVariants(): Array<Any> {
        val variants = resolvers.firstNotNullOfOrNull { it.getVariants() } ?: return arrayOf()

        return variants.map {
            LookupElementBuilder
                .create(it)
                .withAutoCompletionPolicy(AutoCompletionPolicy.SETTINGS_DEPENDENT)
        }.toTypedArray()
    }

    override fun getRangeInElement() = TextRange(0, element.textLength)
}
