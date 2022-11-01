package com.paulpanther.intellijsqueak.fileView

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileSystem
import com.paulpanther.intellijsqueak.wsClient.SqueakClient

class SmalltalkVirtualFileSystem(private val squeak: SqueakClient): VirtualFileSystem() {
    private val listeners = mutableListOf<VirtualFileListener>()
    var root = SmalltalkVirtualFileRoot(this)
    private val changeListeners = mutableListOf<() -> Unit>()

    init {
        refresh(true)
    }

    override fun getProtocol() = "squeak"

    override fun findFileByPath(path: String): VirtualFile? {
        return root.findFile(path)
    }

    override fun refresh(asynchronous: Boolean) {
        squeak.refreshFileSystem(this) {
            changeListeners.forEach { it() }
        }
    }

    fun onChange(listener: () -> Unit) {
        changeListeners += listener
    }

    override fun refreshAndFindFileByPath(path: String): VirtualFile? {
        refresh(false)
        return findFileByPath(path)
    }

    override fun addVirtualFileListener(listener: VirtualFileListener) {
        listeners += listener
    }

    override fun removeVirtualFileListener(listener: VirtualFileListener) {
        listeners.remove(listener)
    }

    override fun deleteFile(requestor: Any?, vFile: VirtualFile) {
        // TODO
    }

    override fun moveFile(
        requestor: Any?,
        vFile: VirtualFile,
        newParent: VirtualFile
    ) {
        // TODO
    }

    override fun renameFile(
        requestor: Any?,
        vFile: VirtualFile,
        newName: String
    ) {
        // TODO
    }

    override fun createChildFile(
        requestor: Any?,
        vDir: VirtualFile,
        fileName: String
    ): VirtualFile {
        TODO("Not yet implemented")
    }

    override fun createChildDirectory(
        requestor: Any?,
        vDir: VirtualFile,
        dirName: String
    ): VirtualFile {
        TODO("Not yet implemented")
    }

    override fun copyFile(
        requestor: Any?,
        virtualFile: VirtualFile,
        newParent: VirtualFile,
        copyName: String
    ): VirtualFile {
        TODO("Not yet implemented")
    }

    override fun isReadOnly() = false
}
