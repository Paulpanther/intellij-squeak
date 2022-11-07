package com.paulpanther.intellijsqueak.wsClient

import com.google.gson.Gson
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.paulpanther.intellijsqueak.fileView.SmalltalkVirtualFileSystem

private abstract class SqueakAction(val type: String)

private class SqueakRefreshFileSystemAction: SqueakAction("refresh_file_system")

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
    val children: List<SqueakRefreshFileSystemResult>,
    val type: String? = null)

data class SqueakFileContentResult(
    val result: String,
    val type: String)

class SqueakClient(parent: Disposable): Disposable {
    private val socket = WebSocketClient()

    init {
        Disposer.register(this, parent)
    }

    fun run() {
        socket.run()
    }

    fun fileContent(clazz: String, file: String): String {
        val result = sendBlocking<SqueakFileContentResult>(SqueakFileContentAction(file, clazz))
        return result.result  // result?
    }

    fun writeFile(clazz: String, file: String, content: String) {
        send(SqueakWriteFileAction(file, clazz, content))
    }

    fun refreshFileSystem(system: SmalltalkVirtualFileSystem, onResult: () -> Unit) {
        send(SqueakRefreshFileSystemAction()) { result: SqueakRefreshFileSystemResult ->
            system.root = result.toVirtualFileRoot(system)
            onResult()
        }
    }

    private inline fun <reified T> send(msg: Any, crossinline onReceive: (response: T) -> Unit) {
        send(msg)

        socket.addAsyncListener {
            val result = Gson().fromJson(it, T::class.java)
            onReceive(result)
        }
    }

    private inline fun <reified T> sendBlocking(msg: Any): T {
        send(msg)
        val msg = socket.addSyncListener()
        return Gson().fromJson(msg, T::class.java)
    }

    fun send(msg: Any) {
        socket.send(Gson().toJson(msg))
    }

    override fun dispose() {
        socket.close()
    }
}
