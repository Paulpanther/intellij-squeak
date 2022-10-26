package com.paulpanther.intellijsqueak

import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.StoredProperty
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import kotlin.reflect.KProperty

inline fun <reified T: PsiElement> PsiElement.findChildrenOfType(): MutableCollection<T> {
    return PsiTreeUtil.findChildrenOfType(this, T::class.java)
}

fun TextRange.shrink(offset: Int) = TextRange(startOffset + 1, endOffset - 1)

operator fun <T> StoredProperty<T>.setValue(
    parent: BaseState,
    property: KProperty<*>,
    s: T
) {
    setValue(parent, s)
}

operator fun <T> StoredProperty<T>.getValue(
    parent: BaseState,
    property: KProperty<*>
): T {
    return getValue(parent)
}
