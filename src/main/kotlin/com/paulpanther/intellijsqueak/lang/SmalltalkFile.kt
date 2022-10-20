package com.paulpanther.intellijsqueak.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.paulpanther.intellijsqueak.SmalltalkFileType
import com.paulpanther.intellijsqueak.SmalltalkLanguage

class SmalltalkFile(viewProvider: FileViewProvider): PsiFileBase(viewProvider, SmalltalkLanguage) {
    override fun getFileType() = SmalltalkFileType()
}
