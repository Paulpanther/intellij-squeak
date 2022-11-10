package com.paulpanther.intellijsqueak.lang.definition

import com.intellij.openapi.fileTypes.LanguageFileType
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons

class SmalltalkFileType: LanguageFileType(SmalltalkLanguage) {
    override fun getName() = "Smalltalk File"
    override fun getDescription() = "Smalltalk File"
    override fun getDefaultExtension() = "st"
    override fun getIcon() = SmalltalkIcons.smalltalk
}
