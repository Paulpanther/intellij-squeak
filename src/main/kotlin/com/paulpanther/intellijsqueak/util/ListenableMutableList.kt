package com.paulpanther.intellijsqueak.util

class ListenableMutableList<T>(values: Collection<T>): ArrayList<T>(values) {
    private val listeners = mutableListOf<() -> Unit>()

    fun onChange(listener: () -> Unit) {
        listeners += listener
    }

    override fun add(element: T): Boolean {
        callListeners()
        return super.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        callListeners()
        return super.addAll(elements)
    }

    private fun callListeners() {
        listeners.forEach { it() }
    }
}

fun <T> listenableMutableListOf(vararg values: T) = ListenableMutableList(values.toMutableList())
