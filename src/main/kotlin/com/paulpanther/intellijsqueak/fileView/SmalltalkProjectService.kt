package com.paulpanther.intellijsqueak.fileView

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.project.Project

@Service
@State(name = "smalltalk-project-state")
class SmalltalkProjectService(
    private val project: Project
): PersistentStateComponent<SmalltalkProjectService.State> {
    private var state = State()

    data class State(
        val projectPackages: MutableList<String> = mutableListOf())

    override fun getState() = state

    override fun loadState(state: State) {
        this.state = state
    }
}
