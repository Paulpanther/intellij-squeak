package com.paulpanther.intellijsqueak.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.*
import com.paulpanther.intellijsqueak.wsClient.SqueakClient
import java.io.File

@Service
@State(
    name = "squeak-global-state",
    storages = [Storage("SqueakPlugin.xml")])
class SqueakAppService: Disposable, PersistentStateComponent<SqueakAppService.State> {
    val client = SqueakClient(this)
    private var state = State()

    data class State(
        private var _isEnabled: Boolean = true,
        var squeakPath: String? = null,
        var squeakImage: String? = null) {

        private val enabledListeners = mutableListOf<(enabled: Boolean) -> Unit>()

        var isEnabled
            get() = _isEnabled
            set(value) {
                _isEnabled = value
                enabledListeners.forEach { it(value) }
            }

        fun onEnabledChanged(listener: (enabled: Boolean) -> Unit) {
            enabledListeners += listener
        }

        fun executableCommand(): List<String>? {
            if (squeakPath == "" || squeakImage == "") return null
            val exeFile = File(squeakPath ?: return null)
            val imageFile = File(squeakImage ?: return null)
            if (!exeFile.exists() || !imageFile.exists()) return null
            return listOf(exeFile.absolutePath, imageFile.absolutePath)
        }
    }

    override fun getState() = state

    override fun loadState(state: State) {
        this.state = state
    }

    override fun dispose() = Unit
}

val squeak get() = service<SqueakAppService>()
