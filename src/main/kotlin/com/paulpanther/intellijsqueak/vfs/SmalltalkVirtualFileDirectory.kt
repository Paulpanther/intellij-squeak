package com.paulpanther.intellijsqueak.vfs

import java.io.InputStream
import java.io.OutputStream

abstract class SmalltalkVirtualFileDirectory<T: SmalltalkVirtualFile>(
    mySystem: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFile?,
    name: String,
    var myChildren: MutableList<T> = mutableListOf()
): SmalltalkVirtualFile(mySystem, parent, name) {

    override fun findFile(path: List<String>): SmalltalkVirtualFile? {
        val childName = path.firstOrNull() ?: return null
        val child = children.find { it.name == childName } ?: return null
        if (path.size == 1) return child
        return child.findFile(path.drop(1))
    }

    override fun isDirectory() = true

    override fun getChildren() = myChildren.toTypedArray<SmalltalkVirtualFile>()

    abstract fun createChild(name: String): Boolean

    fun setChildren(children: List<T>) {
        myChildren = children.toMutableList()
    }

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
