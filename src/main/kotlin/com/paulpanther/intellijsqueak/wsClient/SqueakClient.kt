package com.paulpanther.intellijsqueak.wsClient

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.paulpanther.intellijsqueak.vfs.SmalltalkVirtualFileSystem

private class SqueakFileContentAction(
    val file: String,
    val clazz: String)

private class SqueakWriteFileAction(
    val file: String,
    val clazz: String,
    val content: String)

data class SqueakRefreshFileSystemResult(
    val name: String,
    val children: List<SqueakRefreshFileSystemResult>)

/**
 * Connection to squeak.
 * Methods:
 * - blocking file_content(clazz, file) -> String
 * - write_file(clazz, file, content)
 * - async refresh_file_system -> RefreshFileSystemResult
 * - repeating transcript -> String
 */
class SqueakClient(parent: Disposable): WSClient(parent) {
    init {
        Disposer.register(parent, this)
    }

    fun evaluate(code: String, callback: (result: String) -> Unit) {
        sendAsync("evaluate", code, callback)
    }

    fun fileContent(clazz: String, file: String): String? {
        return sendBlocking("file_content", SqueakFileContentAction(file, clazz))
    }

    fun writeFile(clazz: String, file: String, content: String) {
        sendWithoutResponse("write_file", SqueakWriteFileAction(file, clazz, content))
    }

    fun refreshFileSystem(system: SmalltalkVirtualFileSystem, onResult: () -> Unit) {
        sendAsync<SqueakRefreshFileSystemResult>("refresh_file_system") {
            system.root = it.toVirtualFileRoot(system)
            onResult()
        }
    }

    fun onTranscriptChange(listener: (msg: String) -> Unit) {
        onMessageWithType("transcript") {
            listener(gson.fromJson(it, String::class.java))
        }
    }

    private inline fun <reified T> sendAsync(action: String, msg: Any = "", crossinline callback: (msg: T) -> Unit) {
        sendAsyncWithRawResponse(action, msg) {
            callback(gson.fromJson(it, T::class.java))
        }
    }

    private inline fun <reified T> sendBlocking(action: String, msg: Any = ""): T? {
        val raw = sendBlockingWithRawResponse(action, msg) ?: return null
        return gson.fromJson(raw, T::class.java)
    }
}
