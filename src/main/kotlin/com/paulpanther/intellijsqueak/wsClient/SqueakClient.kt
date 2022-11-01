package com.paulpanther.intellijsqueak.wsClient

import com.google.gson.Gson
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.paulpanther.intellijsqueak.fileView.SmalltalkVirtualFileSystem

private class SqueakAction(
    val type: String)

private fun action(type: String) = SqueakAction(type)

data class SqueakRefreshFileSystemResult(
    val type: String,
    val name: String,
    val children: List<SqueakRefreshFileSystemResult>)

class SqueakClient(parent: Disposable): Disposable {
    private val socket = WebSocketClient()

    init {
        Disposer.register(this, parent)
    }

    fun run() {
        socket.run()
    }

    fun refreshFileSystem(system: SmalltalkVirtualFileSystem, onResult: () -> Unit) {
        val msg = action("refresh_file_system")
        send(msg) { result: SqueakRefreshFileSystemResult ->
            system.root = result.toVirtualFileRoot(system)
            onResult()
        }
    }

    private inline fun <reified T> send(msg: Any, crossinline onReceive: (response: T) -> Unit) {
        send(msg)

        socket.onReceive {
            val result = Gson().fromJson(it, T::class.java)
            onReceive(result)
        }
    }

    fun send(msg: Any) {
        socket.send(Gson().toJson(msg))
    }

    override fun dispose() {
        socket.close()
    }
}
