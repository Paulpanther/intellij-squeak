package com.paulpanther.intellijsqueak.wsClient

import com.paulpanther.intellijsqueak.vfs.*

fun SqueakFileSystemData.toVirtualFileRoot(
    system: SmalltalkVirtualFileSystem
) = SmalltalkVirtualFileRoot(system).also { file ->
    file.setChildren(children.map { it.toVirtualFilePackage(system, file) })
}

fun SqueakPackageData.toVirtualFilePackage(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileRoot
) = SmalltalkVirtualFilePackage(system, parent, name).also { file ->
    file.setChildren(classes.map { it.toVirtualFileClass(system, file) })
}

fun SqueakClassData.toVirtualFileClass(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFilePackage
) = SmalltalkVirtualFileClass(
    system,
    parent,
    name,
    comment,
    instanceVariables,
    classVariables
).also { file ->
    file.setChildren(categories.map { it.toVirtualFileCategory(system, file) })
}

fun SqueakCategoryData.toVirtualFileCategory(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileClass
) = SmalltalkVirtualFileCategory(
    system,
    parent,
    name,
    isInstance
).also { file ->
    file.setChildren(methods.map { it.toVirtualFileMethod(system, file) })
}

fun SqueakMethodData.toVirtualFileMethod(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileCategory
) = SmalltalkVirtualFileMethod(system, parent, name)
