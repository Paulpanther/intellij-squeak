package com.paulpanther.intellijsqueak.editor

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.psi.NavigatablePsiElement

class SmalltalkStructureViewElement(
    private val element: NavigatablePsiElement
): StructureViewTreeElement, SortableTreeElement {
    override fun getPresentation() =
        element.presentation ?: PresentationData()

    override fun getChildren(): Array<TreeElement> {
        return arrayOf()
    }

    override fun navigate(requestFocus: Boolean) {
        element.navigate(requestFocus)
    }

    override fun canNavigate() = element.canNavigate()

    override fun canNavigateToSource(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValue() = element

    override fun getAlphaSortKey() = element.name ?: ""
}
