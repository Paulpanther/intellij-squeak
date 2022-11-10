package com.paulpanther.intellijsqueak.vfs

class SmalltalkVirtualFileCategory(
    system: SmalltalkVirtualFileSystem,
    val clazz: SmalltalkVirtualFileClass,
    name: String,
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileMethod>(system, clazz, name) {
    val methods get() = myChildren.toList()
}
