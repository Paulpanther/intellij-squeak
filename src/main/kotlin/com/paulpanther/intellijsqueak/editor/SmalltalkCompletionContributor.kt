package com.paulpanther.intellijsqueak.editor

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns

class SmalltalkCompletionContributor: CompletionContributor() {
    init {
//        extend(CompletionType.BASIC, PlatformPatterns.psiElement())
    }
}
