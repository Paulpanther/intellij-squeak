package com.paulpanther.intellijsqueak.ui.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import javax.swing.Action

fun dialog(
    project: Project,
    title: String,
    builder: Panel.(dialog: DialogWrapper) -> Unit
) =
    object : DialogWrapper(project, false) {
        init {
            this.title = title
            init()
        }

        override fun createCenterPanel(): DialogPanel {
            val dialog = this
            return panel { builder(this, dialog) }
        }
    }
