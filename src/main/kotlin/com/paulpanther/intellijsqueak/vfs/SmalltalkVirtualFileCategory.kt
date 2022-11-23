package com.paulpanther.intellijsqueak.vfs

import com.paulpanther.intellijsqueak.services.squeak

class SmalltalkVirtualFileCategory(
    system: SmalltalkVirtualFileSystem,
    val classNode: SmalltalkVirtualFileClass,
    name: String,
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileMethod>(system, classNode, name) {
    val methods get() = myChildren.toList()

    override fun createChild(name: String): Boolean {
        val validMethodName = name.matches(Regex("([*+\\\\\\-/~<>@=,%&?!]+)|([a-zA-Z]\\w*)"))
        val canCreate = validMethodName && !methods.any { it.name == name }
        if (!canCreate) return false

        squeak.client.newMethod(classNode.name, this.name, name) {
            refresh(true, false)
        }

        return true
    }

    override fun refresh(
        asynchronous: Boolean,
        recursive: Boolean,
        postRunnable: Runnable?
    ) {
        squeak.client.refreshCategory(this) {
            postRunnable?.run()
            fileSystem.callChangeListeners()
        }
    }
}
