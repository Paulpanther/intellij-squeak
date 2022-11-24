package com.paulpanther.intellijsqueak.ui.toolbars.fileSystem

import com.intellij.ide.bookmark.ui.tree.FolderNodeComparator
import com.intellij.ide.dnd.aware.DnDAwareTree
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.ui.PopupHandler
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.TreeSpeedSearch
import com.intellij.ui.tree.AsyncTreeModel
import com.intellij.ui.tree.StructureTreeModel
import com.intellij.util.EditSourceOnDoubleClickHandler
import com.intellij.util.EditSourceOnEnterKeyHandler
import com.intellij.util.ui.components.BorderLayoutPanel
import com.intellij.util.ui.tree.TreeUtil
import com.paulpanther.intellijsqueak.actions.FileSystemGroup
import com.paulpanther.intellijsqueak.services.SmalltalkProjectService
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileSystem
import com.paulpanther.intellijsqueak.wsClient.SqueakClientStateListener

class SmalltalkFileSystemView(
    project: Project,
    private val useFilter: Boolean = false
): BorderLayoutPanel(), DumbAware, Disposable, DataProvider, SqueakClientStateListener {
    private val fileSystem = SmalltalkVirtualFileSystem()

    private val projectPackages by project.service<SmalltalkProjectService>().state::projectPackages

    private val structure = SmalltalkFileSystemStructure(fileSystem, project, projectPackages, useFilter)
    private val model = StructureTreeModel(structure, FolderNodeComparator(project), this)
    val tree = DnDAwareTree(AsyncTreeModel(model, this))

    val selectedNode
        get() = TreeUtil.getAbstractTreeNode(TreeUtil.getSelectedPathIfOne(tree))

    init {
        squeak.client.register(this)

        addToCenter(ScrollPaneFactory.createScrollPane(tree, true))
        tree.isRootVisible = false
        tree.isHorizontalAutoScrollingEnabled = false
//        DnDSupport.createBuilder(tree)
//            .setDisposableParent(this)
//            .setBeanProvider(handler::createBean)
//            .setDropHandlerWithResult(handler)
//            .setTargetChecker(handler)
//            .enableAsNativeTarget()
//            .install()
//        tree.emptyText.initialize(tree)

        EditSourceOnEnterKeyHandler.install(tree)
        EditSourceOnDoubleClickHandler.install(tree)
        TreeSpeedSearch(tree)
        FileSystemGroup(fileSystem, tree)

        fileSystem.onChange {
            model.invalidate()
            repaint()
        }
    }

    override fun dispose() {

    }

    override fun getData(dataId: String) = when {
        CommonDataKeys.NAVIGATABLE_ARRAY.`is`(dataId) -> navigatableArray()
        else -> null
    }

    private fun navigatableArray(): Array<OpenFileDescriptor> {
        if (selectedNode != null) {
            val node = (selectedNode as? SmalltalkFileSystemNode)?.toOpenFileDescriptor() ?: return arrayOf()
            return arrayOf(node)
        } else {
            return structure.allNavigatables()
        }
    }

    override fun onOpen() {
        fileSystem.refresh(false)
    }

    override fun onClose() {
    }
}
