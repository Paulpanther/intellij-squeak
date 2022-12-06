package com.paulpanther.intellijsqueak.ui.editor

import com.intellij.diff.util.FileEditorBase
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.LabelPosition
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.paulpanther.intellijsqueak.ui.dialog.createAddClassVariableDialog
import com.paulpanther.intellijsqueak.ui.dialog.createAddInstanceVariableDialog
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass
import javax.swing.JPanel

class ClassFileEditor(
    private val project: Project,
    private val file: SmalltalkVirtualFileClass
): FileEditorBase() {
    private val component = ClassFileEditorPanel(project, file)
    override fun getComponent() = component

    override fun getFile() = file

    override fun getName() = file.name
    override fun getPreferredFocusedComponent() = null
}

class ClassFileEditorPanel(
    private val project: Project,
    private val file: SmalltalkVirtualFileClass
): JPanel() {
    private val classVariablesModel = CollectionListModel(file.classVariables)
    private val instanceVariablesModel = CollectionListModel(file.classVariables)
    lateinit var panel: DialogPanel

    init {
        panel = panel {
            onReset {
                classVariablesModel.replaceWith(file.classVariables)
                instanceVariablesModel.replaceWith(file.instanceVariables)

                repaint()
            }

            onApply {
                file.classVariables.replaceWith(classVariablesModel)
                file.instanceVariables.replaceWith(instanceVariablesModel)
            }

            val classVariablesList = ToolbarDecorator
                .createDecorator(JBList(classVariablesModel))
                .setAddAction {
                    val dialog = createAddClassVariableDialog(project, file)
                    if (dialog.showAndGet()) {
                        classVariablesModel.add(dialog.name)
                    }
                }
                .createPanel()

            val instanceVariablesList = ToolbarDecorator
                .createDecorator(JBList(instanceVariablesModel))
                .setAddAction {
                    val dialog = createAddInstanceVariableDialog(project, file)
                    if (dialog.showAndGet()) {
                        classVariablesModel.add(dialog.name)
                    }
                }
                .createPanel()

            row {
                expandableTextField()
                    .bindText(file::comment)
                    .label("Class comment", LabelPosition.TOP)
            }
            row {
                cell(instanceVariablesList)
                    .label("Instance variables", LabelPosition.TOP)
            }
            row {
                cell(classVariablesList)
                    .label("Class variables", LabelPosition.TOP)
            }

            row {
                button("Reset") { panel.reset() }
                button("Apply") { panel.apply() }
            }
        }
        add(panel)
    }
}

private fun <T> CollectionListModel<T>.replaceWith(collection: MutableList<out T>) {
    removeAll()
    addAll(0, collection)
}

private fun <T> MutableList<T>.replaceWith(collection: CollectionListModel<out T>) {
    clear()
    addAll(collection.toList())
}
