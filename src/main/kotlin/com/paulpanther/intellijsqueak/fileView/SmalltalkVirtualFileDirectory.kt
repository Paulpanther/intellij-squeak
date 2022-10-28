package com.paulpanther.intellijsqueak.fileView

import com.intellij.openapi.vfs.VirtualFile
import java.io.InputStream
import java.io.OutputStream

abstract class SmalltalkVirtualFileDirectory(
    mySystem: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFile?,
    name: String,
    val myChildren: MutableList<SmalltalkVirtualFile> = mutableListOf()
): SmalltalkVirtualFile(mySystem, parent, name) {
    override fun findFile(path: String): SmalltalkVirtualFile? {
        val childName = path.split(".").firstOrNull() ?: return null
        return children.find { it.name == childName }
    }

    override fun isDirectory() = true

    override fun getChildren() = myChildren.toTypedArray()

    override fun getOutputStream(
        requestor: Any?,
        newModificationStamp: Long,
        newTimeStamp: Long,
    ): OutputStream = OutputStream.nullOutputStream()

    override fun contentsToByteArray() = byteArrayOf()

    override fun getLength() = 0L

    override fun getInputStream(): InputStream =
        InputStream.nullInputStream()
}
