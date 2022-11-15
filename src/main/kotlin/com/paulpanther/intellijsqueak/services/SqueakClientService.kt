package com.paulpanther.intellijsqueak.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.*
import com.paulpanther.intellijsqueak.wsClient.SqueakClient
import java.io.File

@Service
@State(
    name = "squeak-global-state",
    storages = [Storage("SqueakPlugin.xml")])
class SqueakClientService: Disposable, PersistentStateComponent<SqueakClientService.State> {
    val client = SqueakClient(this)

    private var state = State()

    data class State(
        var squeakPath: String? = null,
        var squeakImage: String? = null,)

    override fun getState() = state

    override fun loadState(state: State) {
        this.state = state
    }

    fun executableCommand(): List<String>? {
        val exeFile = File(state.squeakPath ?: return null)
        val imageFile = File(state.squeakImage ?: return null)
        if (!exeFile.exists() || !imageFile.exists()) return null
        return listOf(exeFile.absolutePath, imageFile.absolutePath)
    }

    override fun dispose() = Unit
}

val squeak get() = service<SqueakClientService>().client
