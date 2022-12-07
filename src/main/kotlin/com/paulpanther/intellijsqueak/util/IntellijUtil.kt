package com.paulpanther.intellijsqueak.util

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.project.Project
import com.intellij.util.application
import java.awt.Component
import java.awt.event.InputEvent

/**
 * Starts a new Thread and displays the progress in the UI
 */
fun <T> runThread(
    title: String,
    canBeCancelled: Boolean,
    callback: (result: T) -> Unit,
    project: Project? = null,
    task: (indicator: ProgressIndicator) -> T
) {
    Thread {
        val indicator = BackgroundableProcessIndicator(project, title, null, "", canBeCancelled)
        ProgressManager.getInstance()
            .runProcess({
                val result = task(indicator)

                application.invokeLater {
                    callback(result)
                }
            }, indicator)
    }.start()
}

val Module.moduleType get() = ModuleType.get(this)

fun Component.executeAction(action: AnAction, place: String, inputEvent: InputEvent? = null, now: Boolean = true) {
    ActionManager.getInstance().tryToExecute(action, inputEvent, this, place, now)
}
