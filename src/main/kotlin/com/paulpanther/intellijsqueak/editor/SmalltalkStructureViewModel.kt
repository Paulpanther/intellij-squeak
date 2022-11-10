package com.paulpanther.intellijsqueak.editor

import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.paulpanther.intellijsqueak.lang.SmalltalkVariable

class SmalltalkStructureViewModel(
    editor: Editor?,
    psiFile: PsiFile
): StructureViewModelBase(
    psiFile,
    editor,
    SmalltalkStructureViewElement(psiFile)
), StructureViewModel.ElementInfoProvider {
    override fun getSorters() = arrayOf(Sorter.ALPHA_SORTER)

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?) =
        false

    override fun isAlwaysLeaf(element: StructureViewTreeElement?): Boolean {
        return element?.value is SmalltalkVariable
    }

    override fun getSuitableClasses(): Array<Class<*>> {
        return arrayOf(SmalltalkVariable::class.java)
    }
}
