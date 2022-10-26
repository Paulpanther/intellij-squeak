package com.paulpanther.intellijsqueak.blocks

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.formatter.common.AbstractBlock

class SmalltalkMethodBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder
) : AbstractBlock(node, wrap, alignment) {

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun isLeaf(): Boolean {
        return node.firstChildNode == null
    }

    override fun getIndent(): Indent? {
        return Indent.getNoneIndent()
    }

    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        var child = node.firstChildNode

//        while (child != null) {
//            if (child.elementType != TokenType.WHITE_SPACE) {
//                blocks += SmalltalkMethodBlock(
//                    child,
//                    Wrap.createWrap(WrapType.NONE, false),
//                    Alignment.createAlignment()
//                )
//            }
//        }
        return mutableListOf()
    }
}
