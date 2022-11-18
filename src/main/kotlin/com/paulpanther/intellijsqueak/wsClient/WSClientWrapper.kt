package com.paulpanther.intellijsqueak.wsClient

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.util.application
import com.paulpanther.intellijsqueak.util.runThread
import org.java_websocket.client.WebSocketClient
import org.java_websocket.enums.ReadyState
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.util.concurrent.TimeUnit
import kotlin.random.Random

data class WSClientRequest(
    val id: Int? = null,
    val action: String,
    val content: Any?)

data class WSClientResponse(
    val id: Int?,
    val type: String?,
    val content: JsonElement,
    val error: String?)

sealed class WSListener {
    abstract fun invoke(res: WSClientResponse): Boolean
}

class WSContinuousListener(
    private val type: String,
    private val callback: (msg: JsonElement) -> Unit
): WSListener() {
    override fun invoke(res: WSClientResponse): Boolean {
        if (res.type == type) callback(res.content)
        return false
    }
}

class WSOnceListener(
    private val id: Int,
    private val callback: (msg: JsonElement) -> Unit
): WSListener() {
    override fun invoke(res: WSClientResponse): Boolean {
        return if (res.id == id) {
            callback(res.content)
            true
        } else false
    }
}

interface SqueakClientStateListener {
    fun onOpen()
    fun onClose()
}

private const val BLOCKING_SEND_TIMEOUT = 2000

/**
 * Handles the WebSocket connection.
 *
 * # Connect
 * You can connect either via `connect()`, which is non-blocking, or via `connection(callback)`,
 * which is async and calls the callback on success/failure.
 * To listen to open and close events register a `SqueakClientStateListener`
 *
 * # Sending Messages
 * There are 3 ways of sending messages.
 * A message can have empty content, but has to have an action string (the "endpoint" to call).
 * **You have to check yourself if client is connected before sending**
 *
 * ## Send without response
 * Sends the message non-blocking
 *
 * ## Send blocking with response
 * Sends the message blocking with a random id.
 * Squeak evaluates it and sends a response with the same id.
 * The client wait for this response and then resumes the thread
 *
 * ## Send async with response
 * Sends the message non-blocking with random id.
 * Squeak evaluates it and sends a response with the same id.
 * The client than calls the callback wrapped in `application.invokeLater`
 */
open class WSClientWrapper(parent: Disposable): WebSocketClient(URI("ws://localhost:2424")), Disposable {
    protected val gson = Gson()

    private val listeners = mutableListOf<WSListener>()
    private val stateListeners = mutableListOf<SqueakClientStateListener>()

    init {
        Disposer.register(parent, this)
    }

    val open get() = isOpen

    fun connect(callback: (success: Boolean) -> Unit) {
        runThread("Connecting to Squeak", false, callback) {
            if (readyState != ReadyState.NOT_YET_CONNECTED) reset()
            connectBlocking(4, TimeUnit.SECONDS)
        }
    }

    fun register(stateListener: SqueakClientStateListener) {
        stateListeners += stateListener
    }

    override fun dispose() {
        close()
    }

    fun sendWithoutResponse(action: String, text: Any?) {
        super.send(gson.toJson(WSClientRequest(null, action, text)))
    }

    fun sendAsyncWithRawResponse(action: String, text: Any?, callback: (result: JsonElement) -> Unit) {
        sendAsyncThreadUnsafe(action, text) {
            application.invokeLater {
                callback(it)
            }
        }
    }

    fun sendBlockingWithRawResponse(action: String, text: Any?): JsonElement? {
        var msg: JsonElement? = null
        val startTime = System.currentTimeMillis()

        sendAsyncThreadUnsafe(action, text) {
            msg = it
        }

        while (msg == null) {
            Thread.sleep(10)
            if (System.currentTimeMillis() - startTime > BLOCKING_SEND_TIMEOUT) break
        }
        return msg
    }

    fun onMessageWithType(type: String, callback: (msg: JsonElement) -> Unit) {
        listeners += WSContinuousListener(type) {
            application.invokeLater {
                callback(it)
            }
        }
    }

    /**
     * Needed because `WebSocketClient#reset` is private and
     * there is no `WebSocketClient#reconnectBlocking(timeout)` method
     */
    private fun reset() {
        val resetMethod = WebSocketClient::class.java.getDeclaredMethod("reset")
        resetMethod.isAccessible = true
        resetMethod.invoke(this)
    }

    /**
     * Unsafe because callback thread is websocket thread
     */
    private fun sendAsyncThreadUnsafe(action: String, text: Any?, callback: (result: JsonElement) -> Unit) {
        val id = nextId()
        val request = gson.toJson(WSClientRequest(id, action, text))

        listenerOnce(id) {
            callback(it)
        }
        send(request)
    }

    private fun listenerOnce(id: Int, callback: (msg: JsonElement) -> Unit) {
        listeners += WSOnceListener(id, callback)
    }

    override fun onMessage(msg: String?) {
        msg ?: return
        val response = gson.fromJson(msg, WSClientResponse::class.java)
        val toRemove = mutableListOf<WSListener>()

        for (listener in listeners) {
            val accepted = listener.invoke(response)
            if (listener is WSOnceListener && accepted) toRemove += listener
        }

        listeners.removeAll(toRemove)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        stateListeners.forEach { it.onClose() }
    }

    override fun onError(ex: Exception?) {
        stateListeners.forEach { it.onClose() }
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        stateListeners.forEach { it.onOpen() }
    }

    private fun nextId(): Int {
        return Random.nextInt()
    }
}
