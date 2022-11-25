package com.paulpanther.intellijsqueak.vfs

import com.intellij.openapi.vfs.VirtualFile
import com.paulpanther.intellijsqueak.lang.definition.SmalltalkFileType
import com.paulpanther.intellijsqueak.ui.SmalltalkIcons
import javax.swing.Icon

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

    abstract fun findFile(path: List<String>): SmalltalkVirtualFile?

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

    abstract override fun delete(requestor: Any?)

    abstract fun icon(): Icon?
}

val SmalltalkVirtualFile.typeName get() = when (this) {
    is SmalltalkVirtualFileMethod -> "method"
    is SmalltalkVirtualFileCategory -> "category"
    is SmalltalkVirtualFileClass -> "class"
    is SmalltalkVirtualFilePackage -> "package"
    else -> error("Could not get type name")
}

val SmalltalkVirtualFile.childIcon get() = when (this) {
    is SmalltalkVirtualFileCategory -> SmalltalkIcons.method
    is SmalltalkVirtualFileClass -> SmalltalkIcons.categoryInstance
    is SmalltalkVirtualFilePackage -> SmalltalkIcons.clazz
    is SmalltalkVirtualFileRoot -> SmalltalkIcons.packageIcon
    else -> null
}
