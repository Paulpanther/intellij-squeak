package com.paulpanther.intellijsqueak

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.paulpanther.intellijsqueak.lang.SmalltalkBlockExpr

class SmalltalkFoldingBuilder: FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(
        root: PsiElement,
        document: Document,
        quick: Boolean
    ): Array<FoldingDescriptor> {
        val group = FoldingGroup.newGroup("block")
        val literals = root.findChildrenOfType<SmalltalkBlockExpr>()
        return literals
            .filter { it.statementBody.children.isNotEmpty() }
            .map { FoldingDescriptor(it.node, it.textRange.shrink(1), group) }
            .toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode) = "..."

    override fun isCollapsedByDefault(node: ASTNode) = false
}
