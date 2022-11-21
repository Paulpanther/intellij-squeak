package com.paulpanther.intellijsqueak.ui.widgets

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.util.Consumer
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.settings.SqueakToolsConfigurable
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons
import com.paulpanther.intellijsqueak.ui.dialog.ConfirmDialog
import com.paulpanther.intellijsqueak.util.runThread
import com.paulpanther.intellijsqueak.wsClient.SqueakClientStateListener
import java.awt.event.MouseEvent

class SqueakWidgetFactory : StatusBarWidgetFactory {
    override fun getId() = "Squeak"
    override fun getDisplayName() = "Squeak"
    override fun createWidget(project: Project) = SqueakWidget()
    override fun disposeWidget(widget: StatusBarWidget) = widget.dispose()
    override fun canBeEnabledOn(statusBar: StatusBar) = true
    override fun isAvailable(project: Project) = true
}

/**
 * UI Widget in the bottom right corner.
 * Clicking on it initiates connection to squeak.
 */
class SqueakWidget: StatusBarWidget, StatusBarWidget.IconPresentation, SqueakClientStateListener {

    private var statusBar: StatusBar? = null

    init {
        squeak.client.register(this)
        squeak.state.onEnabledChanged {
            statusBar?.updateWidget(ID())
        }
    }

    override fun getTooltipText() = "Configure Squeak Image"
    override fun getIcon() = if (highlight()) SmalltalkIcons.squeakSelected else SmalltalkIcons.squeak
    override fun dispose() = Unit
    override fun ID() = "Squeak"
    override fun getPresentation() = this

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
    }

    private fun highlight() = squeak.state.isEnabled && squeak.client.open

    override fun getClickConsumer() = Consumer<MouseEvent> {
        connectSqueak()
    }

    /**
     * 1. Check if connection is open
     * 2. Try to connect
     * 3. Start squeak
     * 4. Try to connect
     */
    private fun connectSqueak() {
        if (!squeak.state.isEnabled) squeak.state.isEnabled = true

        // Check if squeak is running and has connection
        if (squeak.client.open) {
            showNotification("Already connected", NotificationType.INFORMATION)
            return
        }

        squeak.client.connect { open ->
            if (!open) {
                val proceed = askUserShouldStartSqueak()
                if (proceed) {
                    startSqueakExe { started ->
                        if (started) {
                            // Retry because when server starts the first try will throw error
                            squeak.client.connect(retryOnError = true) { open2 ->
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
            val startCommand = squeak.state.executableCommand()

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

    override fun onOpen() {
        statusBar?.updateWidget(ID())
    }

    override fun onClose() {
        statusBar?.updateWidget(ID())
    }
}
