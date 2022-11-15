package com.paulpanther.intellijsqueak.vfs

import java.io.InputStream
import java.io.OutputStream

abstract class SmalltalkVirtualFileDirectory<T: SmalltalkVirtualFile>(
    mySystem: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFile?,
    name: String,
    var myChildren: MutableList<T> = mutableListOf()
): SmalltalkVirtualFile(mySystem, parent, name) {
    override fun findFile(path: String): SmalltalkVirtualFile? {
        val childName = path.split(".").firstOrNull() ?: return null
        return children.find { it.name == childName }
    }

    override fun isDirectory() = true

    override fun getChildren() = myChildren.toTypedArray<SmalltalkVirtualFile>()

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

    override fun refresh(
        asynchronous: Boolean,
        recursive: Boolean,
        postRunnable: Runnable?
    ) {
        for (child in children) {
            child.refresh(asynchronous, recursive, postRunnable)
        }
    }
}