package com.paulpanther.intellijsqueak.ui.editor

import com.intellij.diff.util.FileEditorBase
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.LabelPosition
import com.intellij.ui.dsl.builder.panel
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass
import javax.swing.JComponent

class ClassFileEditor(
    private val project: Project,
    private val file: SmalltalkVirtualFileClass
): FileEditorBase() {
    override fun getComponent(): JComponent {
        val varList = JBList<String>("Hello", "World")
        var listToolbar = ActionManager.getInstance().createActionToolbar("Smalltalk@ClassEditor", )

        return panel {
            row {
                textArea()
                    .resizableColumn()
                    .label("Class comment", LabelPosition.TOP)
            }
            row {
                cell(varList)
                    .label("Instance variables", LabelPosition.TOP)
            }
        }
    }

    override fun getName() = file.name
    override fun getPreferredFocusedComponent() = null
}
