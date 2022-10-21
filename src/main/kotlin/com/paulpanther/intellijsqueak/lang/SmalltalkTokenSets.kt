package com.paulpanther.intellijsqueak.lang

import com.intellij.psi.tree.TokenSet

object SmalltalkTokenSets {
    val comment = TokenSet.create(SmalltalkTypes.COMMENT)
    val identifier = TokenSet.create(SmalltalkTypes.IDENTIFIER)
}
