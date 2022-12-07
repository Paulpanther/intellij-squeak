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

fun createAddClassVariableDialog(
    project: Project,
    clazz: SmalltalkVirtualFileClass
) =
    AddDialog(project, "Add Class Variable") { name ->
        when {
            name == "" -> ValidationInfo("A name must be specified")
            name in clazz.classVariables -> ValidationInfo("Name '$name' does already exist")
            !isValidClassVariableName(name) -> ValidationInfo("Name '$name' is not a valid variable name")
            else -> null
        }
    }
