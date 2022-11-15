package com.paulpanther.intellijsqueak.wsClient

import com.google.gson.Gson
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.util.application
import com.paulpanther.intellijsqueak.util.runInThread
import java.util.concurrent.TimeUnit
import kotlin.random.Random

data class WSClientRequest(
    val id: Int,
    val content: String)

data class WSClientResponse(
    val id: Int?,
    val type: String?,
    val content: String)

sealed class WSListener {
    abstract fun invoke(res: WSClientResponse): Boolean
}

class WSContinuousListener(
    private val type: String,
    private val callback: (msg: String) -> Unit
): WSListener() {
    override fun invoke(res: WSClientResponse): Boolean {
        if (res.type == type) callback(res.content)
        return false
    }
}

class WSOnceListener(
    private val id: Int,
    private val callback: (msg: String) -> Unit
): WSListener() {
    override fun invoke(res: WSClientResponse): Boolean {
        return if (res.id == id) {
            callback(res.content)
            true
        } else false
    }
}

open class WSClientWrapper(parent: Disposable): Disposable {
    private val client = WSClient(this::onMessage, this::rawOnClose)
    protected val gson = Gson()

    private val listeners = mutableListOf<WSListener>()
    private val closeListeners = mutableListOf<() -> Unit>()

    init {
        Disposer.register(parent, this)
    }

    val open get() = client.isOpen

    fun connect() {
        client.connect()
    }

    fun connect(callback: (success: Boolean) -> Unit) {
        runInThread("Connecting to Squeak", false, callback) {
            client.connectBlocking(4, TimeUnit.SECONDS)
        }
    }

    fun onClose(callback: () -> Unit) {
        closeListeners += callback
    }

    override fun dispose() {
        client.close()
    }

    fun send(text: String) {
        client.send(text)
    }

    fun send(text: String, callback: (result: String) -> Unit) {
        sendUnsafe(text) {
            application.invokeLater {
                callback(it)
            }
        }
    }

    fun sendBlocking(text: String): String {
        var msg: String? = null
        sendUnsafe(text) {
            msg = it
        }

        while (msg == null) Thread.sleep(10)
        return msg ?: error("Msg cannot be null")
    }

    fun onMessageWithType(type: String, callback: (msg: String) -> Unit) {
        listeners += WSContinuousListener(type) {
            application.invokeLater {
                callback(it)
            }
        }
    }

    private fun sendUnsafe(text: String, callback: (result: String) -> Unit) {
        val id = nextId()
        val request = gson.toJson(WSClientRequest(id, text))

        listenerOnce(id) {
            callback(it)
        }
        client.send(request)
    }

    private fun listenerOnce(id: Int, callback: (msg: String) -> Unit) {
        listeners += WSOnceListener(id, callback)
    }

    private fun onMessage(msg: String) {
        val response = gson.fromJson(msg, WSClientResponse::class.java)
        val toRemove = mutableListOf<WSListener>()

        for (listener in listeners) {
            val accepted = listener.invoke(response)
            if (listener is WSOnceListener && accepted) toRemove += listener
        }

        listeners.removeAll(toRemove)
    }

    private fun rawOnClose() {
        closeListeners.forEach { it() }
    }

    private fun nextId(): Int {
        return Random.nextInt()
    }
}
