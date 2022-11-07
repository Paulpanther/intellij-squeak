package com.paulpanther.intellijsqueak.fileView

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.ide.util.treeView.AbstractTreeStructure
import com.intellij.ide.util.treeView.NodeDescriptor
import com.intellij.navigation.NavigationRequest
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.tree.LeafState
import com.paulpanther.intellijsqueak.SmalltalkIcons
import com.paulpanther.intellijsqueak.wsClient.SqueakClient


class SmalltalkFileSystemNode(
    project: Project,
    val file: SmalltalkVirtualFile,
    val parent: SmalltalkFileSystemNode?
) : AbstractTreeNode<String>(project, file.name) {

    override fun update(presentation: PresentationData) {
        presentation.presentableText = file.name
        presentation.setIcon(SmalltalkIcons.file)
        presentation.addText(
            "${presentation.presentableText}  ",
            SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES
        )
    }

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> {
        return file.children
            .map {
                SmalltalkFileSystemNode(project, it as SmalltalkVirtualFile, this)
            }.toMutableList()
    }

    override fun getVirtualFile(): VirtualFile {
        return file
    }

    override fun canNavigate() = true

    override fun canNavigateToSource() =
        file is SmalltalkVirtualFileMethod

    override fun isAlwaysLeaf() =
        file is SmalltalkVirtualFileMethod

    fun toOpenFileDescriptor(): OpenFileDescriptor {
        return OpenFileDescriptor(project, virtualFile)
    }
}

/**
 * TODO OpenFileAction.openFile
 */
class SmalltalkFileSystemStructure(
    private val system: SmalltalkVirtualFileSystem,
    private val project: Project,
): AbstractTreeStructure() {

    override fun getRootElement(): Any {
        return SmalltalkFileSystemNode(project, system.root, null)
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
