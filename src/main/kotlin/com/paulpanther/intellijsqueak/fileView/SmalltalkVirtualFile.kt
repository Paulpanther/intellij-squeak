package com.paulpanther.intellijsqueak.fileView

import com.intellij.openapi.vfs.VirtualFile
import com.paulpanther.intellijsqueak.SmalltalkFileType

sealed class SmalltalkVirtualFile(
    private val mySystem: SmalltalkVirtualFileSystem,
    private val myParent: SmalltalkVirtualFile?,
    private val myName: String
): VirtualFile() {
    override fun getFileSystem() = mySystem

    override fun getFileType() = SmalltalkFileType()

    override fun getPath(): String {
        val parentPath = parent?.path ?: ""
        return "$parentPath.$name"
    }

    abstract fun findFile(path: String): SmalltalkVirtualFile?

    override fun getName() = myName

    override fun getParent(): VirtualFile? = myParent

    override fun isWritable(): Boolean {
        // TODO
        return true
    }

    override fun isValid(): Boolean {
        // TODO
        return true
    }

    override fun getTimeStamp(): Long {
        // TODO
        return 0L
    }

    override fun refresh(
        asynchronous: Boolean,
        recursive: Boolean,
        postRunnable: Runnable?
    ) {
        TODO("Not yet implemented")
    }
}
