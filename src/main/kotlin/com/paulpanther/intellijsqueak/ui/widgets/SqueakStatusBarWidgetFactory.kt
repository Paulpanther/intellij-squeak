package com.paulpanther.intellijsqueak

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.util.Consumer
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons
import java.awt.event.MouseEvent

class SqueakStatusBarWidgetFactory: StatusBarWidgetFactory {
    override fun getId() = "Squeak"
    override fun getDisplayName() = "Squeak"
    override fun isAvailable(project: Project) = true
    override fun createWidget(project: Project) = SqueakStatusBarWidget()
    override fun disposeWidget(widget: StatusBarWidget) = widget.dispose()
    override fun canBeEnabledOn(statusBar: StatusBar) = true
}

class SqueakStatusBarWidget: StatusBarWidget, StatusBarWidget.IconPresentation {
    private lateinit var statusBar: StatusBar

    override fun getTooltipText() = "Configure Squeak Image"
    override fun getIcon() = SmalltalkIcons.squeak
    override fun dispose() = Unit
    override fun ID() = "Squeak"
    override fun getPresentation() = this

    override fun getClickConsumer() =
        Consumer<MouseEvent> {

        }

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
    }
}
