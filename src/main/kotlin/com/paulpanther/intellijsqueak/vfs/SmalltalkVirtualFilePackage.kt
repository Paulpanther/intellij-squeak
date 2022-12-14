package com.paulpanther.intellijsqueak.vfs

import com.intellij.util.application
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons

class SmalltalkVirtualFilePackage(
    system: SmalltalkVirtualFileSystem,
    val root: SmalltalkVirtualFileRoot,
    name: String
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileClass>(system, root, name) {
    val classes get() = myChildren.toList()

    override fun createChild(name: String): Boolean {
        val validClassName = name.matches(Regex("[A-Z]\\w*"))
        val canCreate = validClassName && !classes.any { it.name == name }
        if (!canCreate) return false

        squeak.client.newClass(this, name) {
            refresh(true, false)
        }

        return true
    }

    override fun delete(requestor: Any?) {
        squeak.client.removePackage(this) {
            root.refresh(true, false)
        }
    }

    override fun icon() = SmalltalkIcons.packageIcon

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

    override fun renameFile(newName: String) {
        squeak.client.renamePackage(this, newName) {
            application.invokeLater {
                root.refresh(true, false)
            }
        }
    }
}
