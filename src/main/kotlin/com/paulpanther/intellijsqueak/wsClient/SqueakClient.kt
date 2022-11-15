package com.paulpanther.intellijsqueak.wsClient

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileSystem

private open class SqueakAction(val type: String)

private class SqueakFileContentAction(
    val file: String,
    val clazz: String
): SqueakAction("file_content")

private class SqueakWriteFileAction(
    val file: String,
    val clazz: String,
    val content: String
): SqueakAction("write_file")

data class SqueakRefreshFileSystemResult(
    val name: String,
    val children: List<SqueakRefreshFileSystemResult>)

class SqueakClient(parent: Disposable): WSClientWrapper(parent) {

    init {
        Disposer.register(parent, this)
    }

    fun fileContent(clazz: String, file: String): String {
        return sendBlocking(SqueakFileContentAction(file, clazz))
    }

    fun writeFile(clazz: String, file: String, content: String) {
        send(SqueakWriteFileAction(file, clazz, content))
    }

    fun refreshFileSystem(system: SmalltalkVirtualFileSystem, onResult: () -> Unit) {
        send<SqueakRefreshFileSystemResult>(SqueakAction("refresh_file_system")) {
            system.root = it.toVirtualFileRoot(system)
            onResult()
        }
    }

    fun onTranscriptChange(listener: (msg: String) -> Unit) {
        onMessageWithType("transcript", listener)
    }

    private fun send(msg: Any) {
        send(gson.toJson(msg))
    }

    private inline fun <reified T> send(msg: Any, crossinline callback: (msg: T) -> Unit) {
        send(gson.toJson(msg)) {
            callback(gson.fromJson(it, T::class.java))
        }
    }

    private inline fun <reified T> sendBlocking(msg: Any): T {
        val raw = sendBlocking(gson.toJson(msg))
        return gson.fromJson(raw, T::class.java)
    }
}
