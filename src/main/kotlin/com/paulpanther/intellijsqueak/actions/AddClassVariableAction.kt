package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons
import com.paulpanther.intellijsqueak.ui.dialog.AddClassVariableDialog

class AddClassVariableAction: AnAction("Add Class or Instance Variable", "Adds a new class or instance variable", SmalltalkIcons.propertiesInstance) {
    override fun actionPerformed(e: AnActionEvent) {
        val currentClass = e.getData(FileActionKeys.currentClass) ?: return

        val dialog = AddClassVariableDialog(e.project ?: return, currentClass)
        if (dialog.showAndGet()) {
            currentClass.classVariables += dialog.variableName
        }
    }
}
