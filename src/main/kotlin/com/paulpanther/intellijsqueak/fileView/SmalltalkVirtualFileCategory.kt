package com.paulpanther.intellijsqueak.fileView

class SmalltalkVirtualFileCategory(
    system: SmalltalkVirtualFileSystem,
    val clazz: SmalltalkVirtualFileClass,
    name: String,
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileMethod>(system, clazz, name) {
    val methods get() = myChildren.toList()
}
