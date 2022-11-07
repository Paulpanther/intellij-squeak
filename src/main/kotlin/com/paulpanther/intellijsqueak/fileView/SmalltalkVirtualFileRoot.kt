package com.paulpanther.intellijsqueak.fileView

class SmalltalkVirtualFileRoot(
    system: SmalltalkVirtualFileSystem
): SmalltalkVirtualFileDirectory<SmalltalkVirtualFilePackage>(system, null, "squeak") {
    val packages get() = myChildren.toList()
}
