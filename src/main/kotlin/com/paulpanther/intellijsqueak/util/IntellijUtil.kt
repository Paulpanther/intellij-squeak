package com.paulpanther.intellijsqueak.util

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

fun runProgress(
    title: String,
    canBeCancelled: Boolean,
    project: Project? = null,
    task: ProgressIndicator.() -> Unit
) {
    ProgressManager.getInstance().run(
        object: Task.Modal(project, title, canBeCancelled) {
            override fun run(indicator: ProgressIndicator) {
                task(indicator)
            }
        }
    )
}
