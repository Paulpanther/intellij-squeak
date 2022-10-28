package com.paulpanther.intellijsqueak.fileView

import java.io.InputStream
import java.io.OutputStream

class SmalltalkVirtualFileMethod (
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileCategory,
    name: String
): SmalltalkVirtualFile(system, parent, name) {
    override fun findFile(path: String): SmalltalkVirtualFile? {
        if (path == "") return this
        return null
    }

    override fun isDirectory() = false

    override fun getChildren() = null

    override fun getOutputStream(
        requestor: Any?,
        newModificationStamp: Long,
        newTimeStamp: Long
    ): OutputStream {
        return OutputStream.nullOutputStream()
    }

    override fun contentsToByteArray(): ByteArray {
        return "Hello".toByteArray()
    }

    override fun getLength(): Long {
        return contentsToByteArray().size.toLong()
    }

    override fun getModificationStamp(): Long {
        return 0
    }

    override fun getInputStream(): InputStream {
        return "Hello".byteInputStream()
    }
}
