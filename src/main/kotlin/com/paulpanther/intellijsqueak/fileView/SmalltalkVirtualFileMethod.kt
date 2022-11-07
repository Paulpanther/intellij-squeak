package com.paulpanther.intellijsqueak.fileView

import com.intellij.openapi.fileEditor.FileDocumentManager
import java.io.InputStream
import java.io.OutputStream

class SmalltalkVirtualFileMethod (
    system: SmalltalkVirtualFileSystem,
    val category: SmalltalkVirtualFileCategory,
    name: String
): SmalltalkVirtualFile(system, category, name) {
    private val originalContent by lazy {
        squeak.fileContent(clazz.name, name)
    }
    private var modifiedContent: String? = null
    private val content get() = modifiedContent ?: originalContent

    val clazz get() = category.clazz

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
        val manager = requestor as? FileDocumentManager ?: return OutputStream.nullOutputStream()
        val doc = manager.getDocument(this)
        if (doc != null && manager.isDocumentUnsaved(doc)) {
            val content = doc.text
            squeak.writeFile(clazz.name, name, content)
            modifiedContent = content
        }
        return OutputStream.nullOutputStream()
    }

    override fun contentsToByteArray(): ByteArray {
        return content.toByteArray()
    }

    override fun getLength(): Long {
        return contentsToByteArray().size.toLong()
    }

    override fun refresh(
        asynchronous: Boolean,
        recursive: Boolean,
        postRunnable: Runnable?
    ) {
    }

    override fun getModificationStamp(): Long {
        return 0
    }

    override fun getInputStream(): InputStream {
        return content.byteInputStream()
    }
}
