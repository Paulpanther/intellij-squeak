package com.paulpanther.intellijsqueak.vfs

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.util.application
import com.paulpanther.intellijsqueak.lang.SmalltalkMethod
import com.paulpanther.intellijsqueak.services.squeak
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons
import java.io.InputStream
import java.io.OutputStream

class SmalltalkVirtualFileMethod (
    system: SmalltalkVirtualFileSystem,
    val category: SmalltalkVirtualFileCategory,
    name: String
): SmalltalkVirtualFile(system, category, name) {
    private var _originalContentOrNull: String? = null
    private val originalContent: String? get() =
        if (_originalContentOrNull != null) {
            _originalContentOrNull
        } else {
            _originalContentOrNull = squeak.client.fileContent(clazz, this)
            _originalContentOrNull
        }

    private var modifiedContent: String? = null
    private val content get() = modifiedContent ?: originalContent

    val clazz get() = category.classNode

    val isInstance get() = category.isInstance

    fun findPsiMethod(project: Project) = PsiManager.getInstance(project).findFile(this)?.firstChild as? SmalltalkMethod

    override fun findFile(path: List<String>): SmalltalkVirtualFile? {
        return if (path.isEmpty()) this else null
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

            val formattedContent = content.lines().joinToString("\r", transform = ::replaceSpacesWithTabs)

            if (squeak.client.open) squeak.client.writeFile(clazz, this, formattedContent)
            modifiedContent = content
        }
        return OutputStream.nullOutputStream()
    }

    private fun replaceSpacesWithTabs(line: String): String {
        val content = line.dropWhile { it == ' ' }
        val spaces = line.length - content.length
        val tabs = spaces / 4
        return (0 until tabs).joinToString("") { "\t" } + content
    }

    override fun contentsToByteArray(): ByteArray {
        return content?.toByteArray() ?: "".toByteArray()
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
        return content?.byteInputStream() ?: "".byteInputStream()
    }

    override fun delete(requestor: Any?) {
        squeak.client.removeMethod(clazz, this) {
            application.invokeLater {
                category.refresh(true, false)
            }
        }
    }

    override fun renameFile(newName: String) {
        squeak.client.renameMethod(clazz, this, newName) {
            application.invokeLater {
                category.refresh(true, false)
            }
        }
    }

    override fun icon() = SmalltalkIcons.method
}
