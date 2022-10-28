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


class SmalltalkFileSystemNode(project: Project, value: String, val file: SmalltalkVirtualFile) :
    AbstractTreeNode<String>(project, value) {

    override fun update(presentation: PresentationData) {
        presentation.presentableText = value
        presentation.setIcon(SmalltalkIcons.file)
        presentation.addText("${presentation.presentableText}  ", SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES)
    }

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> {
        return arrayListOf()
    }

    override fun getVirtualFile(): VirtualFile {
        return file
    }

    override fun navigate(requestFocus: Boolean) {
        println("Navigate")
        super.navigate(requestFocus)
    }

    override fun navigationRequest(): NavigationRequest? {
        println("Can navigate")
        return super.navigationRequest()
    }

    override fun canNavigate(): Boolean {
        return true
    }

    override fun canNavigateToSource(): Boolean {
        return true
    }

    override fun isAlwaysLeaf(): Boolean {
        return true
    }
}

/**
 * TODO OpenFileAction.openFile
 */
class SmalltalkFileSystemStructure(private val project: Project) : AbstractTreeStructure() {
    override fun getRootElement(): Any {
        val system = SmalltalkVirtualFileSystem()
        return SmalltalkFileSystemNode(project, "Hello", system.methodFile)
    }

    fun navigateableArray(): Array<OpenFileDescriptor> {
        return arrayOf(OpenFileDescriptor(project, (rootElement as SmalltalkFileSystemNode).file))
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
