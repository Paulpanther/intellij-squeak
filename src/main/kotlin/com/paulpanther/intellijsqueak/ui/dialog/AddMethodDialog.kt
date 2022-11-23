package com.paulpanther.intellijsqueak.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFile
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileCategory
import javax.swing.Action
import javax.swing.JComponent

class AddMethodDialog(
    project: Project,
    private val parent: SmalltalkVirtualFileCategory
): DialogWrapper(project, false) {
    init {
        title = "Add Method to Category ${parent.name}"
        init()
    }

    override fun createActions(): Array<Action> = arrayOf()

    override fun createCenterPanel(): JComponent {
        return panel {
            row {
                val field = textField().component
                field.addActionListener {
                    val success = parent.createChild(field.text)
                    if (success) {
                        doOKAction()
                    } else {
                        // TODO
                        println("Error")
                    }
                }
            }
        }
    }
}
