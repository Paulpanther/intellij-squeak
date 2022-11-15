package com.paulpanther.intellijsqueak.wsClient

import com.intellij.util.application
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.ConnectException
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

private class AsyncSqueakListener(
    val onlyOnce: Boolean,
    val callback: (msg: String) -> Boolean)

class WebSocketClient {
    private val outgoingQueue = LinkedBlockingQueue<String>()
    private val incomingQueue = LinkedBlockingQueue<String>()

    private var asyncListeners = mutableListOf<AsyncSqueakListener>()
    private var syncListener: ((msg: String) -> Unit)? = null

    private val running = AtomicBoolean(true)

    var hadError = false
        private set
    var connected = false
        private set

    private val client = HttpClient(CIO) {
        install(WebSockets)
        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, _ ->
                if (cause is ConnectException && cause.message == "Connection refused") {
                    hadError = true
                    connected = false
                    // TODO
                }
            }
        }
    }

    fun run() {
        hadError = false
        connected = false

        Thread {
            runBlocking {
                launch {
                    client.webSocket(
                        method = HttpMethod.Get,
                        host = "localhost",
                        port = 2424
                    ) {
                        connected = true

                        while (running.get()) {
                            while (outgoingQueue.isNotEmpty()) {
                                send(Frame.Text(outgoingQueue.remove()))
                            }

                            val msg = incoming.tryReceive()
                            if (msg.isClosed) {
                                hadError = true
                                connected = false
                                break
                            }

                            val frame =
                                msg.getOrNull() as? Frame.Text ?: continue
                            val text = frame.readText()

                            callListeners(text)
                        }
                    }
                }
            }
        }.start()
    }

    fun close() {
        connected = false
        running.set(false)
    }

    fun send(msg: String) {
        outgoingQueue += msg
    }

    fun addAsyncListener(onlyOnce: Boolean, listener: (msg: String) -> Boolean) {
        this.asyncListeners += AsyncSqueakListener(onlyOnce, listener)
    }

    private fun callListeners(msg: String) {
//        if (this.syncListener != null) {
//            this.syncListener(msg)
//        }

        application.invokeLater {
            for (listener in this.asyncListeners) {
                val stop = listener.callback(msg)
                if (listener.onlyOnce) this.asyncListeners -= listener
                if (stop) break
            }
        }
    }

    fun addSyncListener(accept: (msg: String) -> Boolean): String {
        this.syncListener = {
            incomingQueue += it
        }

        while (incomingQueue.isEmpty()) continue

        this.syncListener = null
        return incomingQueue.remove().also {
//            incomingQueue.
        }
    }
}
