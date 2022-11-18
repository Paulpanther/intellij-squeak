package com.paulpanther.intellijsqueak.ui.widgets

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.util.Consumer
import com.paulpanther.intellijsqueak.services.SqueakClientService
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.settings.SqueakToolsConfigurable
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons
import com.paulpanther.intellijsqueak.ui.dialog.ConfirmDialog
import com.paulpanther.intellijsqueak.util.runThread
import java.awt.event.MouseEvent

class SqueakWidgetFactory : StatusBarWidgetFactory {
    override fun getId() = "Squeak"
    override fun getDisplayName() = "Squeak"
    override fun isAvailable(project: Project) = true
    override fun createWidget(project: Project) = SqueakWidget()
    override fun disposeWidget(widget: StatusBarWidget) = widget.dispose()
    override fun canBeEnabledOn(statusBar: StatusBar) = true
}

class SqueakWidget : StatusBarWidget, StatusBarWidget.IconPresentation {
    override fun getTooltipText() = "Configure Squeak Image"
    override fun getIcon() = SmalltalkIcons.squeak
    override fun dispose() = Unit
    override fun ID() = "Squeak"
    override fun getPresentation() = this
    override fun install(statusBar: StatusBar) = Unit

    override fun getClickConsumer() =
        Consumer<MouseEvent> {
            connectSqueak()
        }

    private fun connectSqueak() {
        // Check if squeak is running and has connection
        if (squeak.open) {
            showNotification("Already connected", NotificationType.INFORMATION)
            return
        }

        squeak.connect { open ->
            if (!open) {
                val proceed = askUserShouldStartSqueak()
                if (proceed) {
                    startSqueakExe { started ->
                        if (started) {
                            squeak.connect { open2 ->
                                if (open2) {
                                    showNotification(
                                        "Connected to Squeak image",
                                        NotificationType.INFORMATION
                                    )
                                } else {
                                    showNotification(
                                        "Could not open connection",
                                        NotificationType.ERROR
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun askUserShouldStartSqueak(): Boolean {
            return ConfirmDialog(
                "No image",
                "No running Squeak image found",
                "Start Squeak"
            ).showAndGet()
    }

    private fun startSqueakExe(callback: (success: Boolean) -> Unit) {
        runThread("Starting Squeak", false, callback) {
            val startCommand = service<SqueakClientService>().executableCommand()

            if (startCommand == null) {
                ShowSettingsUtil.getInstance()
                    .showSettingsDialog(null, SqueakToolsConfigurable::class.java)
                false
            } else {
                val process = ProcessBuilder(startCommand).start()
                if (!process.isAlive) {
                    showNotification(
                        "Could not start Squeak. Check the executable and image path",
                        NotificationType.ERROR)
                    false
                } else {
                    Thread.sleep(2000)
                    true
                }
            }
        }
    }

    private fun showNotification(text: String, type: NotificationType) {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup("Squeak Notification Group")
            .createNotification(text, type)
            .notify(null)
    }
}
