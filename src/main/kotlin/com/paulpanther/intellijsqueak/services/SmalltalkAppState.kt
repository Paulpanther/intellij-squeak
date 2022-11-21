package com.paulpanther.intellijsqueak.services

import com.intellij.openapi.components.*

// TODO refactor into client service
@Service
@State(name = "smalltalk-app-state",
    storages = [Storage("SqueakAppPlugin.xml")])
class SmalltalkAppState: PersistentStateComponent<SmalltalkAppState> {
    private val enabledListeners: MutableList<(enabled: Boolean) -> Unit> = mutableListOf()
    var enabled = false
        private set

    data class State(
        var enabled: Boolean = false)

    fun onEnabledChanged(listener: (enabled: Boolean) -> Unit) {
        this.enabledListeners += listener
    }

    fun setEnabled(enabled: Boolean) {
        this.enabledListeners.forEach { it(enabled) }
    }

    override fun getState() = this

    override fun loadState(state: SmalltalkAppState) {
        this.enabled = state.enabled
    }
}

val smalltalkState get() = service<SmalltalkAppState>()
