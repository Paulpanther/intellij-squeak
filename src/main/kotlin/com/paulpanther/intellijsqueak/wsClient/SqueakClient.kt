package com.paulpanther.intellijsqueak.wsClient

import com.google.gson.Gson
import com.intellij.openapi.Disposable
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.impl.BackgroundableProcessIndicator
import com.intellij.openapi.util.Disposer
import com.paulpanther.intellijsqueak.util.runProgress
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileSystem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

private data class SqueakResponse(
    val type: String,
    val content: String)

class SqueakClient(parent: Disposable): Disposable {
    private val socket = WebSocketClient()

    init {
        Disposer.register(parent, this)
    }

    val connected by socket::connected

    fun run() {
        socket.run()
    }

    fun tryRun(callback: (success: Boolean) -> Unit) {
        runProgress("Connecting to Squeak")

//            socket.run()
//
//            while (indicator.isRunning) {
//                Thread.sleep(500)
//                if (socket.connected) {
//                    callback(true)
//                    break
//                }
//            }
//        }, indicator)
    }

    fun fileContent(clazz: String, file: String): String {
        val result = sendBlocking<SqueakFileContentResult>(SqueakFileContentAction(file, clazz))
        return result.result  // result?
    }

    fun writeFile(clazz: String, file: String, content: String) {
        send(SqueakWriteFileAction(file, clazz, content))
    }

    fun refreshFileSystem(system: SmalltalkVirtualFileSystem, onResult: () -> Unit) {
//        send(SqueakRefreshFileSystemAction()) { result: SqueakRefreshFileSystemResult ->
//            system.root = result.toVirtualFileRoot(system)
//            onResult()
//        }
    }

    fun onTranscriptChange(listener: (msg: String) -> Unit) {
//        socket.addAsyncListener {
//        }
    }

//    private inline fun <reified T> send(msg: Any, crossinline callback: (response: SqueakResponse) -> Unit) {
//        send(msg)
//
//        addAsyncListener(true, callback)
//    }

    private inline fun <reified T> sendBlocking(msg: Any): T {
        send(msg)
        val msg = addSyncListener()
        return Gson().fromJson(msg, T::class.java)
    }

    private fun addAsyncListener(onlyOnce: Boolean, callback: (msg: SqueakResponse) -> Boolean) {
        socket.addAsyncListener(onlyOnce) {
            val result = Gson().fromJson(it, SqueakResponse::class.java)
            callback(result)
        }
    }

    private fun addSyncListener(): String {
        socket.addSyncListener { false }
        return ""
    }

    fun send(msg: Any) {
        socket.send(Gson().toJson(msg))
    }

    override fun dispose() {
        socket.close()
    }
}
