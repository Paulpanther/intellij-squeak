package com.paulpanther.intellijsqueak.vfs

import com.intellij.openapi.vfs.VirtualFile
import com.paulpanther.intellijsqueak.lang.definition.SmalltalkFileType

sealed class SmalltalkVirtualFile(
    private val mySystem: SmalltalkVirtualFileSystem,
    private val myParent: SmalltalkVirtualFile?,
    protected var myName: String
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
}
