package com.paulpanther.intellijsqueak.vfs

import com.paulpanther.intellijsqueak.services.squeak

class SmalltalkVirtualFileClass(
    system: SmalltalkVirtualFileSystem,
    val packageNode: SmalltalkVirtualFilePackage,
    name: String
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileCategory>(system, packageNode, name) {
    val categories get() = myChildren.toList()

    override fun createChild(name: String): Boolean {
        val canCreate = !categories.any { it.name == name }
        if (!canCreate) return false

        squeak.client.newCategory(this.name, name) {
            refresh(true, false)
        }

        return true
    }

    override fun refresh(
        asynchronous: Boolean,
        recursive: Boolean,
        postRunnable: Runnable?
    ) {
        squeak.client.refreshClass(this) {
            postRunnable?.run()
            fileSystem.callChangeListeners()
        }
    }
}
