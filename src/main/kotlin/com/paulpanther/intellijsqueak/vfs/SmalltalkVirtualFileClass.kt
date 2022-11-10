package com.paulpanther.intellijsqueak.vfs

class SmalltalkVirtualFileClass(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFilePackage,
    name: String
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileCategory>(system, parent, name) {
    val categories get() = myChildren.toList()
}
