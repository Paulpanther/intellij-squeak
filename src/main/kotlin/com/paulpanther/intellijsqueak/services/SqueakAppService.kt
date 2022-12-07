package com.paulpanther.intellijsqueak.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.*
import com.paulpanther.intellijsqueak.ui.toolbars.fileSystem.SmalltalkFileSystemView
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileSystem
import com.paulpanther.intellijsqueak.wsClient.SqueakClient
import java.io.File

@Service
@State(
    name = "squeak-app-state",
    storages = [Storage("SqueakPluginApp.xml")])
class SqueakAppService: Disposable, PersistentStateComponent<SqueakAppService.State> {
    val fileSystem by lazy { SmalltalkVirtualFileSystem() }

    val client = SqueakClient(this)
    private var state = State()
    private val enabledListeners = mutableListOf<(enabled: Boolean) -> Unit>()

    data class State(
        var _isEnabled: Boolean = true,
        var squeakPath: String? = null,
        var squeakImage: String? = null)

    var isEnabled
        get() = state._isEnabled
        set(value) {
            state._isEnabled = value
            enabledListeners.forEach { it(value) }
        }

    fun onEnabledChanged(listener: (enabled: Boolean) -> Unit) {
        enabledListeners += listener
    }

    fun executableCommand(): List<String>? {
        if (state.squeakPath == "" || state.squeakImage == "") return null
        val exeFile = File(state.squeakPath ?: return null)
        val imageFile = File(state.squeakImage ?: return null)
        if (!exeFile.exists() || !imageFile.exists()) return null
        return listOf(exeFile.absolutePath, imageFile.absolutePath)
    }

    override fun getState() = state

    override fun loadState(state: State) {
        this.state = state
    }

    override fun dispose() = Unit
}

val squeak get() = service<SqueakAppService>()
