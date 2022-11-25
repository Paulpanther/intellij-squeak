package com.paulpanther.intellijsqueak.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFile
import javax.swing.Action
import javax.swing.JComponent

class RenameFileDialog(
    project: Project,
    childName: String,
    private val file: SmalltalkVirtualFile
): DialogWrapper(project, false) {
    init {
        this.title = "Rename $childName"
        init()
    }

    override fun createActions(): Array<Action> = arrayOf()

    override fun createCenterPanel(): JComponent {
        return panel {
            row {
                val field = textField().component
                field.addActionListener {
                    file.renameFile(field.text)
                    doOKAction()
                }
            }
        }
    }
}
