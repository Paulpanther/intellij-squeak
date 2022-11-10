package com.paulpanther.intellijsqueak

import com.intellij.openapi.fileTypes.LanguageFileType

class SmalltalkFileType: LanguageFileType(SmalltalkLanguage) {
    override fun getName() = "Smalltalk File"
    override fun getDescription() = "Smalltalk File"
    override fun getDefaultExtension() = "st"
    override fun getIcon() = SmalltalkIcons.smalltalk
}
