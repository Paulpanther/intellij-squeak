package com.paulpanther.intellijsqueak.vfs

import com.paulpanther.intellijsqueak.services.squeak
import javax.swing.Icon

class SmalltalkVirtualFileRoot(
    system: SmalltalkVirtualFileSystem
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFilePackage>(system, null, "squeak") {
    val packages get() = myChildren.toList()

    override fun createChild(name: String): Boolean {
        val canCreate = !packages.any { it.name == name }
        if (!canCreate) return false

        squeak.client.newPackage(name) {
            refresh(true, false)
        }

        return true
    }

    override fun delete(requestor: Any?) {}
    override fun icon() = null

    override fun refresh(
        asynchronous: Boolean,
        recursive: Boolean,
        postRunnable: Runnable?
    ) {
        fileSystem.refresh(asynchronous)
    }
}
