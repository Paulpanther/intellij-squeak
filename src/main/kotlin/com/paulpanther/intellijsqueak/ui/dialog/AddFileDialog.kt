package com.paulpanther.intellijsqueak.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileDirectory
import javax.swing.Action
import javax.swing.JComponent

class AddFileDialog(
    project: Project,
    childName: String,
    parentName: String?,
    private val parent: SmalltalkVirtualFileDirectory<*>
): DialogWrapper(project, false) {
    init {
        this.title = "Create $childName${parentName?.let { "for $parentName ${parent.name}" } ?: ""}"
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
