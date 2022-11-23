package com.paulpanther.intellijsqueak.vfs

import com.paulpanther.intellijsqueak.services.squeak

class SmalltalkVirtualFilePackage(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileRoot,
    name: String
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileClass>(system, parent, name) {
    val classes get() = myChildren.toList()

    override fun createChild(name: String): Boolean {
        val validClassName = name.matches(Regex("[A-Z]\\w*"))
        val canCreate = validClassName && !classes.any { it.name == name }
        if (!canCreate) return false

        squeak.client.newClass(this.name, name) {
            refresh(true, false)
        }

        return true
    }

    override fun refresh(
        asynchronous: Boolean,
        recursive: Boolean,
        postRunnable: Runnable?
    ) {
        squeak.client.refreshPackage(this) {
            postRunnable?.run()
            fileSystem.callChangeListeners()
        }
    }
}
