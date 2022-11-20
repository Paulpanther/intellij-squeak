package com.paulpanther.intellijsqueak.lang.definition

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import com.intellij.openapi.util.Key
import com.intellij.psi.impl.source.resolve.FileContextUtil

val SMALLTALK_CONSOLE_KEY = Key.create<Boolean>("Smalltalk.Console")

class SmalltalkParserUtil: GeneratedParserUtilBase() {
    companion object {

        @JvmStatic
        fun isMethod(builder: PsiBuilder, level: Int): Boolean {
            return !isConsole(builder, level)
        }

        @JvmStatic
        fun isConsole(builder: PsiBuilder, level: Int): Boolean {
            val file = builder.getUserData(FileContextUtil.CONTAINING_FILE_KEY)?.virtualFile
            return file != null && file.getUserData(SMALLTALK_CONSOLE_KEY) == true
        }
    }
}
