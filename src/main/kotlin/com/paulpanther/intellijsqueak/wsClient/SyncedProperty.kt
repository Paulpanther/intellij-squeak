package com.paulpanther.intellijsqueak.wsClient

import kotlin.reflect.KProperty

class SyncedProperty<T>(
    default: T,
    private val getter: () -> T?,
    private val setter: (v: T) -> Unit = {},
) {
    private var backing: T = default

    operator fun getValue(ref: Any?, property: KProperty<*>): T {
        getter()?.let { backing = it }
        return backing
    }

    operator fun setValue(ref: Any?, property: KProperty<*>, value: T) {
        backing = value
        setter(value)
    }
}
