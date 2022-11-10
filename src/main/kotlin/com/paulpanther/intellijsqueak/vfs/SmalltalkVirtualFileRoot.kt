package com.paulpanther.intellijsqueak.vfs

class SmalltalkVirtualFileRoot(
    system: SmalltalkVirtualFileSystem
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFilePackage>(system, null, "squeak") {
    val packages get() = myChildren.toList()
}
