package com.paulpanther.intellijsqueak.fileView

import com.intellij.ide.bookmark.ui.tree.FolderNodeComparator
import com.intellij.ide.dnd.aware.DnDAwareTree
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.tree.AsyncTreeModel
import com.intellij.ui.tree.StructureTreeModel
import com.intellij.util.EditSourceOnDoubleClickHandler
import com.intellij.util.ui.components.BorderLayoutPanel
import com.paulpanther.intellijsqueak.wsClient.SqueakClient

class SmalltalkFileSystemView(project: Project)
    : BorderLayoutPanel(), DumbAware, Disposable, DataProvider {
    private val squeak = SqueakClient(this).apply { run() }
    private val fileSystem = SmalltalkVirtualFileSystem(squeak)

    private val structure = SmalltalkFileSystemStructure(fileSystem, project)
    private val model = StructureTreeModel(structure, FolderNodeComparator(project), this)
    val tree = DnDAwareTree(AsyncTreeModel(model, this))

    init {
        addToCenter(ScrollPaneFactory.createScrollPane(tree, true))
        tree.isHorizontalAutoScrollingEnabled = false
//        DnDSupport.createBuilder(tree)
//            .setDisposableParent(this)
//            .setBeanProvider(handler::createBean)
//            .setDropHandlerWithResult(handler)
//            .setTargetChecker(handler)
//            .enableAsNativeTarget()
//            .install()
//        tree.emptyText.initialize(tree)
        EditSourceOnDoubleClickHandler.install(tree)

        fileSystem.onChange {
            repaint()
            model.invalidate()
        }
    }

    override fun dispose() {

    }

    override fun getData(dataId: String) = when {
        CommonDataKeys.NAVIGATABLE_ARRAY.`is`(dataId) -> structure.navigateableArray()
        else -> null
    }
}
