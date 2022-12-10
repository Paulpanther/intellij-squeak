package com.paulpanther.intellijsqueak.lang.references.message

import com.intellij.psi.PsiElement

interface MessageReferenceHelper {
    fun resolve(): PsiElement?
    fun getVariants(): List<String>?
}
