package com.paulpanther.intellijsqueak.lang.definition

import com.intellij.psi.tree.TokenSet
import com.paulpanther.intellijsqueak.lang.SmalltalkTypes

object SmalltalkTokenSets {
    val comment = TokenSet.create(SmalltalkTypes.COMMENT)
    val identifier = TokenSet.create(SmalltalkTypes.IDENTIFIER)
}
