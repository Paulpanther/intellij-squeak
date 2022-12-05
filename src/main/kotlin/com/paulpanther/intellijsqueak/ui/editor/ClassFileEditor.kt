package com.paulpanther.intellijsqueak.ui.editor

import com.intellij.diff.util.FileEditorBase
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.project.Project
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.LabelPosition
import com.intellij.ui.dsl.builder.panel
import com.paulpanther.intellijsqueak.actions.FileActionKeys
import com.paulpanther.intellijsqueak.actions.SmalltalkActions
import com.paulpanther.intellijsqueak.util.executeAction
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass
import javax.swing.JPanel

class ClassFileEditor(
    private val project: Project,
    private val file: SmalltalkVirtualFileClass
): FileEditorBase() {
    override fun getComponent() = ClassFileEditorPanel(project, file)

    override fun getFile() = file

    override fun getName() = file.name
    override fun getPreferredFocusedComponent() = null
}

class ClassFileEditorPanel(
    private val project: Project,
    private val file: SmalltalkVirtualFileClass
): JPanel(), DataProvider {
    init {
        add(panel {
            val model = CollectionListModel(file.classVariables)
            val varList = JBList(model)

            file.classVariables.onChange {
                varList.invalidate()
                repaint()
            }

            val listWithDecorator = ToolbarDecorator
                .createDecorator(varList)
                .setAddAction { this@ClassFileEditorPanel.executeAction(
                    SmalltalkActions.addClassVariableAction,
                    "ClassFileEditor@AddVariables")
                }
                .disableRemoveAction()
                .createPanel()

            row {
                textArea()
                    .resizableColumn()
                    .label("Class comment", LabelPosition.TOP)
            }
            row {
                cell(listWithDecorator)
                    .label("Instance variables", LabelPosition.TOP)
            }
        })
    }

    override fun getData(dataId: String): Any? = when (dataId) {
        FileActionKeys.currentClass.name -> file
        else -> null
    }

}
