package com.paulpanther.intellijsqueak.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.LabelPosition
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.paulpanther.intellijsqueak.lang.definition.isValidClassVariableName
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass
import javax.swing.JComponent
import javax.swing.JTextField

class AddClassVariableDialog(
    project: Project,
    private val clazz: SmalltalkVirtualFileClass
): DialogWrapper(project, false) {
    private val nameField = JBTextField()
    val variableName: String get() = nameField.text

    init {
        title = "Add Class Variable"
        init()
    }

    override fun doValidate() = when {
        variableName == "" -> ValidationInfo("A name must be specified")
        variableName in clazz.classVariables -> ValidationInfo("Name '$variableName' does already exist")
        !isValidClassVariableName(variableName) -> ValidationInfo("Name '$variableName' is not a valid variable name")
        else -> null
    }

    override fun createCenterPanel() = panel {
        row {
            cell(nameField)
                .label("Variable name", LabelPosition.TOP)
        }
    }
}
