package com.paulpanther.intellijsqueak.actions

import com.intellij.openapi.actionSystem.DataKey
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileClass

object FileActionKeys {
    val currentClass = DataKey.create<SmalltalkVirtualFileClass>("Smalltalk.currentClass")
}
