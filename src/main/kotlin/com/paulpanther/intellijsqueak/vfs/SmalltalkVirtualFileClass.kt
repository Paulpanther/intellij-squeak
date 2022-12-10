package com.paulpanther.intellijsqueak.vfs

import com.intellij.util.application
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons

class SmalltalkVirtualFileClass(
    system: SmalltalkVirtualFileSystem,
    val packageNode: SmalltalkVirtualFilePackage,
    name: String,
    var comment: String,
    val instanceVariables: List<String>,
    val classVariables: List<String>,
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileCategory>(system, packageNode, name) {
    val categories get() = myChildren.toList()

    val instanceMethods get() = categories.filter { it.isInstance }.flatMap { it.methods }
    val classMethods get() = categories.filter { !it.isInstance }.flatMap { it.methods }

//    override fun isDirectory() = false

    override fun getModificationStamp() = 0L

    override fun createChild(name: String): Boolean {
        val canCreate = !categories.any { it.name == name }
        if (!canCreate) return false

        squeak.client.newCategory(this, name, true) {
            refresh(true, false)
        }

        return true
    }

    override fun delete(requestor: Any?) {
        squeak.client.removeClass(this) {
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
        squeak.client.renameClass(this, newName) {
            application.invokeLater {
                packageNode.refresh(true, false)
            }
        }
    }
}
