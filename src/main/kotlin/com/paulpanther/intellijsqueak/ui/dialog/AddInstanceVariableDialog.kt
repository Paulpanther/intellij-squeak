package com.paulpanther.intellijsqueak.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.paulpanther.intellijsqueak.lang.definition.isValidClassVariableName
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass

fun createAddInstanceVariableDialog(
    project: Project,
    clazz: SmalltalkVirtualFileClass
) = AddDialog(project, "Add Instance Variable") { name ->
    when {
        name == "" -> ValidationInfo("A name must be specified")
        name in clazz.instanceVariables -> ValidationInfo("Name '$name' does already exist")
        !isValidClassVariableName(name) -> ValidationInfo("Name '$name' is not a valid variable name")
        else -> null
    }
}
