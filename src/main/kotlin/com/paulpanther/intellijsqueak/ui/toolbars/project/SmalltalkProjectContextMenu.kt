package com.paulpanther.intellijsqueak.ui.toolbars.project

import com.intellij.ide.ui.customization.CustomActionsSchema
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.impl.PopupMenuPreloader
import com.intellij.openapi.project.DumbAware
import com.intellij.ui.PopupHandler
import com.intellij.util.ui.tree.TreeUtil
import javax.swing.JTree

class SmalltalkProjectContextMenu(
    private val tree: JTree
): DumbAware, ActionGroup() {
    override fun getChildren(event: AnActionEvent?): Array<AnAction> {
        val paths = TreeUtil.getSelectedPathsIfAll(tree) { it.parentPath != null }
        val group = CustomActionsSchema.getInstance().getCorrectedAction("SmalltalkProject.TookWindow.PopupMenu") as? ActionGroup
        return group?.getChildren(event) ?: EMPTY_ARRAY
    }

    init {
        val place = "popup@SmalltalkProjectView"
        val handler = PopupHandler.installPopupMenu(tree, this, place)
        PopupMenuPreloader.install(tree, place, handler) { this }
    }
}
