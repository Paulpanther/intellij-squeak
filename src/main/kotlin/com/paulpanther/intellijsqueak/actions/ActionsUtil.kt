package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager

fun action(id: String) = ActionManager.getInstance().getAction(id) ?: error("No action with id '$id'")

fun actionGroup(id: String): ActionGroup {
    return ActionManager.getInstance().getAction(id) as? ActionGroup ?: error("Could not get ActionGroup with id '$id'")
}
