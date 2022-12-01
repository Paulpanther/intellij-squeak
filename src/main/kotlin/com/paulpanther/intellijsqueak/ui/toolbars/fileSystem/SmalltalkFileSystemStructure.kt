package com.paulpanther.intellijsqueak.ui.toolbars.fileSystem

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.ide.util.treeView.AbstractTreeStructure
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.ui.SimpleTextAttributes
import com.paulpanther.intellijsqueak.vfs.*


class SmalltalkFileSystemNode(
    project: Project,
    val file: SmalltalkVirtualFile,
    val parent: SmalltalkFileSystemNode?,
    private val structure: SmalltalkFileSystemStructure
) : AbstractTreeNode<String>(project, file.name) {

    override fun update(presentation: PresentationData) {
        presentation.presentableText = file.name
        presentation.setIcon(file.icon())
        presentation.addText(
            "${presentation.presentableText}  ",
            SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES
        )
    }

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> {
        val children = if (structure.useFilter && file is SmalltalkVirtualFilePackage) {
            file.children.filter { it.name in structure.packageFilter }
        } else {
            file.children.toList()
        }

        return children
            .map {
                SmalltalkFileSystemNode(project, it as SmalltalkVirtualFile, this, structure)
            }.toMutableList()
    }

    override fun getVirtualFile() = file

    override fun canNavigate() = true

    override fun canNavigateToSource() =
        file is SmalltalkVirtualFileMethod || file is SmalltalkVirtualFileClass

    override fun isAlwaysLeaf() =
        file is SmalltalkVirtualFileMethod

    fun toOpenFileDescriptor() =
        OpenFileDescriptor(project, virtualFile)
}

class SmalltalkFileSystemStructure(
    private val system: SmalltalkVirtualFileSystem,
    private val project: Project,
    val packageFilter: List<String> = listOf(),
    val useFilter: Boolean = false
): AbstractTreeStructure() {

    override fun getRootElement(): Any {
        return SmalltalkFileSystemNode(project, system.root, null, this)
    }

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

    fun allNavigatables(): Array<OpenFileDescriptor> {
        return arrayOf(
            OpenFileDescriptor(
                project,
                (rootElement as SmalltalkFileSystemNode).file
            )
        )
    }

    override fun commit() = Unit
    override fun hasSomethingToCommit() = false
}
