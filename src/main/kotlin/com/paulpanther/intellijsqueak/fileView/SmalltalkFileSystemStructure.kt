package com.paulpanther.intellijsqueak.fileView

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.ide.util.treeView.AbstractTreeStructure
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleTextAttributes
import com.paulpanther.intellijsqueak.SmalltalkIcons


class SmalltalkFileSystemNode(project: Project, value: String) :
    AbstractTreeNode<String>(project, value) {

    override fun update(presentation: PresentationData) {
        presentation.presentableText = value
        presentation.setIcon(SmalltalkIcons.file)
        presentation.addText("${presentation.presentableText}  ", SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES)
    }

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> {
        return arrayListOf()
    }
}

class SmalltalkFileSystemStructure(private val project: Project) : AbstractTreeStructure() {
    override fun getRootElement(): Any {
        return SmalltalkFileSystemNode(project, "Hello")
    }

    override fun getChildElements(element: Any): Array<Any> {
        return arrayOf()
    }

    override fun getParentElement(element: Any): Any? {
        return null
    }

    override fun createDescriptor(
        element: Any,
        parentDescriptor: NodeDescriptor<*>?
    ): NodeDescriptor<*> {
        return element as NodeDescriptor<*>
    }

    override fun commit() = Unit
    override fun hasSomethingToCommit() = false
}
