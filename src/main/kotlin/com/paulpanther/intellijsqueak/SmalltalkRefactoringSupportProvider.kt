package com.paulpanther.intellijsqueak

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable

class SmalltalkRefactoringSupportProvider: RefactoringSupportProvider() {
    override fun isMemberInplaceRenameAvailable(
        element: PsiElement,
        context: PsiElement?
    ): Boolean {
        return element is SmalltalkVariable
    }
}
