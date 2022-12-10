package com.paulpanther.intellijsqueak.vfs

import com.intellij.util.application
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons
import com.paulpanther.intellijsqueak.wsClient.SyncedProperty

class SmalltalkVirtualFileClass(
    system: SmalltalkVirtualFileSystem,
    val packageNode: SmalltalkVirtualFilePackage,
    name: String
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileCategory>(system, packageNode, name) {
    var comment by SyncedProperty("", { squeak.client.getClassComment(this) }, { })

    val instanceVariables by lazy { squeak.client.getInstanceVariables(this)?.toMutableList() ?: mutableListOf() }
    val classVariables by lazy { squeak.client.getClassVariables(this)?.toMutableList() ?: mutableListOf() }

    val categories get() = myChildren.toList()

    val instanceMethods get() = categories.flatMap { it.methods }
    val classMethods get() = listOf<SmalltalkVirtualFileMethod>()

//    override fun isDirectory() = false

    override fun getModificationStamp() = 0L

    override fun createChild(name: String): Boolean {
        val canCreate = !categories.any { it.name == name }
        if (!canCreate) return false

        squeak.client.newCategory(this.name, name) {
            refresh(true, false)
        }

        return true
    }

    override fun delete(requestor: Any?) {
        squeak.client.removeClass(this.name) {
            packageNode.refresh(true, false)
        }
    }

    override fun icon() = SmalltalkIcons.clazz

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

    override fun renameFile(newName: String) {
        squeak.client.renameClass(name, newName) {
            application.invokeLater {
                packageNode.refresh(true, false)
            }
        }
    }
}
