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

class WebSocketClient {
    private val outgoingQueue = LinkedBlockingQueue<String>()
    private val incomingQueue = LinkedBlockingQueue<String>()

    private var listeners = mutableListOf<(msg: String) -> Unit>()

    private val running = AtomicBoolean(true)

    private val client = HttpClient(CIO) {
        install(WebSockets)
        HttpResponseValidator {
            handleResponseExceptionWithRequest { cause, _ ->
                if (cause is ConnectException && cause.message == "Connection refused") {
                    // TODO
                }
            }
        }
    }

    fun run() {
        Thread {
            runBlocking {
                launch {
                    client.webSocket(
                        method = HttpMethod.Get,
                        host = "localhost",
                        port = 2424
                    ) {
                        while (running.get()) {
                            while (outgoingQueue.isNotEmpty()) {
                                send(Frame.Text(outgoingQueue.remove()))
                            }

                            val msg = incoming.tryReceive()
                            if (msg.isClosed) break
                            val frame =
                                msg.getOrNull() as? Frame.Text ?: continue
                            val text = frame.readText()

                            listeners.forEach { it(text) }
                        }
                    }
                }
            }
        }.start()
    }

    fun close() {
        running.set(false)
    }

    fun send(msg: String) {
        outgoingQueue += msg
    }

    fun addAsyncListener(listener: (msg: String) -> Unit) {
        this.listeners += {
            application.invokeLater {
                listener(it)
            }
        }
    }

    fun addSyncListener(): String {
        val l = { msg: String ->
            incomingQueue += msg
        }
        this.listeners += l

        while (incomingQueue.isEmpty()) continue

        this.listeners -= l
        return incomingQueue.remove()
    }
}
