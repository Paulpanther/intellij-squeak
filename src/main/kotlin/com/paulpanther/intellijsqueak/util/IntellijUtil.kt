package com.paulpanther.intellijsqueak.util

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.project.Project
import com.intellij.util.application

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
