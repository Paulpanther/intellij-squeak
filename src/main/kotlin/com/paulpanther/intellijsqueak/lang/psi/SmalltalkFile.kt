package com.paulpanther.intellijsqueak.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.util.Key
import com.intellij.psi.FileViewProvider
import com.paulpanther.intellijsqueak.lang.definition.SmalltalkFileType
import com.paulpanther.intellijsqueak.lang.definition.SmalltalkLanguage

class SmalltalkFile(viewProvider: FileViewProvider): PsiFileBase(viewProvider, SmalltalkLanguage) {
    override fun getFileType() = SmalltalkFileType()

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
        println(key)
        super.putUserData(key, value)
    }
}
