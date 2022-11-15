package com.paulpanther.intellijsqueak.ui.dialog

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.panel

class ConfirmDialog(
    title: String,
    private val label: String,
    confirmText: String,
): DialogWrapper(true) {
    init {
        this.title = title
        setOKButtonText(confirmText)
        init()
    }

    override fun createCenterPanel() = panel {
        row {
            label(label)
        }
    }
}
