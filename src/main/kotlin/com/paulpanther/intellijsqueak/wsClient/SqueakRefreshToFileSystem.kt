package com.paulpanther.intellijsqueak.wsClient

import com.paulpanther.intellijsqueak.fileView.*

fun SqueakRefreshFileSystemResult.toVirtualFileRoot(
    system: SmalltalkVirtualFileSystem
) = SmalltalkVirtualFileRoot(system).also { file ->
    file.setChildren(children.map { it.toVirtualFilePackage(system, file) })
}

fun SqueakRefreshFileSystemResult.toVirtualFilePackage(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileRoot
) = SmalltalkVirtualFilePackage(system, parent, name).also { file ->
    file.setChildren(children.map { it.toVirtualFileClass(system, file) })
}

fun SqueakRefreshFileSystemResult.toVirtualFileClass(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFilePackage
) = SmalltalkVirtualFileClass(system, parent, name).also { file ->
    file.setChildren(children.map { it.toVirtualFileCategory(system, file) })
}

fun SqueakRefreshFileSystemResult.toVirtualFileCategory(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileClass
) = SmalltalkVirtualFileCategory(system, parent, name).also { file ->
    file.setChildren(children.map { it.toVirtualFileMethod(system, file) })
}

fun SqueakRefreshFileSystemResult.toVirtualFileMethod(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileCategory
) = SmalltalkVirtualFileMethod(system, parent, name)
