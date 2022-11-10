package com.paulpanther.intellijsqueak.editor

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.paulpanther.intellijsqueak.lang.SmalltalkTypes

class SmalltalkBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = arrayOf(
        BracePair(SmalltalkTypes.BLOCK_START, SmalltalkTypes.BLOCK_END, true)
    )

    override fun isPairedBracesAllowedBeforeType(
        lbraceType: IElementType,
        contextType: IElementType?
    ) = true

    override fun getCodeConstructStart(
        file: PsiFile?,
        openingBraceOffset: Int
    ): Int {
        // There is no owner so we return the same offset
        return openingBraceOffset
    }
}
