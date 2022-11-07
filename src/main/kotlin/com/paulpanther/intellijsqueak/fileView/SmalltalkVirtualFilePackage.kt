package com.paulpanther.intellijsqueak.fileView

class SmalltalkVirtualFilePackage(
    system: SmalltalkVirtualFileSystem,
    parent: SmalltalkVirtualFileRoot,
    name: String
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFileClass>(system, parent, name) {
    val classes get() = myChildren.toList()
}
