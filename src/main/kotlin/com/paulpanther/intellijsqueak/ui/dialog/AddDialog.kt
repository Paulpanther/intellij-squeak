package com.paulpanther.intellijsqueak.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.panel

class AddDialog(
    project: Project,
    title: String,
    private val validate: (name: String) -> ValidationInfo?
): DialogWrapper(project, false) {
    private val nameField = JBTextField()
    val name: String get() = nameField.text

    init {
        this.title = title
        init()
    }

    override fun doValidate(): ValidationInfo? {
        return validate(name)
    }

    override fun getPreferredFocusedComponent() = nameField

    override fun createCenterPanel() = panel {
        row {
            cell(nameField)
        }
    }
}
