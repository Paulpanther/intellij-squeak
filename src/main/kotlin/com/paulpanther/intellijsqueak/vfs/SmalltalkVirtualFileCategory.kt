package com.paulpanther.intellijsqueak.vfs

import com.intellij.util.application
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons

class SmalltalkVirtualFileCategory(
    system: SmalltalkVirtualFileSystem,
    val classNode: SmalltalkVirtualFileClass,
    name: String,
    val isInstance: Boolean
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileMethod>(system, classNode, name) {
    val methods get() = myChildren.toList()

    override fun createChild(name: String): Boolean {
        val binaryMethodName = name.matches(Regex("[*+\\\\\\-/~<>@=,%&?!]+"))
        val unaryMethodName = name.matches(Regex("[a-zA-Z]\\w*"))
        val keywordMethodName = name.matches(Regex("([a-zA-Z]\\w*:\\s*)+"))

        val sanitizedName = if (keywordMethodName) {
            name.replace(" ", "")
        } else name

        val canCreate = (binaryMethodName || unaryMethodName || keywordMethodName) && !methods.any { it.name == sanitizedName }
        if (!canCreate) return false

        squeak.client.newMethod(classNode, this, sanitizedName) {
            refresh(true, false)
        }

        return true
    }

    override fun delete(requestor: Any?) {
        squeak.client.removeCategory(classNode, this) {
            classNode.refresh(true, false)
        }
    }

    override fun icon() =
        if (isInstance) SmalltalkIcons.categoryInstance
        else SmalltalkIcons.categoryClass

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

    override fun renameFile(newName: String) {
        squeak.client.renameCategory(classNode, this, newName) {
            application.invokeLater {
                classNode.refresh(true, false)
            }
        }
    }
}
