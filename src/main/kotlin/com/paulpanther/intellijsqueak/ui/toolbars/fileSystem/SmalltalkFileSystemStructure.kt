package com.paulpanther.intellijsqueak.ui.toolbars.fileSystem

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.ide.util.treeView.AbstractTreeStructure
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleTextAttributes
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.vfs.*


class SmalltalkFileSystemNode(
    project: Project,
    val file: SmalltalkVirtualFile,
    val parent: SmalltalkFileSystemNode?,
    private val structure: SmalltalkFileSystemStructure
) : AbstractTreeNode<String>(project, file.name) {
    private val children by lazy {
        file.children
            .map {
                SmalltalkFileSystemNode(project, it as SmalltalkVirtualFile, this, structure)
            }.toMutableList()
    }


    override fun update(presentation: PresentationData) {
        presentation.presentableText = file.name
        presentation.setIcon(file.icon())
        presentation.addText(
            "${presentation.presentableText}  ",
            SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES
        )
    }

    fun findNodeWithFile(file: SmalltalkVirtualFile): SmalltalkFileSystemNode? {
        if (this.file == file) return this
        for (child in children) {
            child.findNodeWithFile(file)?.let { return it }
        }
        return null
    }

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> =
        children

    override fun getVirtualFile() = file

    override fun canNavigate() = true

    override fun canNavigateToSource() =
        file is SmalltalkVirtualFileMethod

    override fun isAlwaysLeaf() =
        file is SmalltalkVirtualFileMethod

    fun toOpenFileDescriptor() =
        OpenFileDescriptor(project, virtualFile)
}

class SmalltalkFileSystemStructure(
    private val project: Project,
) : AbstractTreeStructure() {
    private val root by lazy {
        SmalltalkFileSystemNode(
            project,
            squeak.fileSystem.root,
            null,
            this
        )
    }

    fun navigatableForClass(clazz: SmalltalkVirtualFileClass): NavigationItem? {
        return root.findNodeWithFile(clazz)
    }

    override fun getRootElement() = root

    override fun getChildElements(element: Any): Array<Any> {
        val node = element as? AbstractTreeNode<*> ?: return arrayOf()
        return node.children.toTypedArray()
    }

    override fun getParentElement(element: Any): Any? {
        val node = element as? SmalltalkFileSystemNode ?: return null
        return node.parent
    }

    override fun createDescriptor(
        element: Any,
        parentDescriptor: NodeDescriptor<*>?
    ): NodeDescriptor<*> {
        return element as NodeDescriptor<*>
    }

    fun allNavigatables() =
        arrayOf(OpenFileDescriptor(project, rootElement.file))

    override fun commit() = Unit
    override fun hasSomethingToCommit() = false
}
