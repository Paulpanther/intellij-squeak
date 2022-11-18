package com.paulpanther.intellijsqueak.wsClient

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class WSClient(
    private val messageCallback: (msg: String) -> Unit,
    private val openCallback: () -> Unit,
    private val closeCallback: () -> Unit
): WebSocketClient(URI("ws://localhost:2424")) {

    override fun onOpen(handshake: ServerHandshake?) {
        openCallback()
    }

    override fun onMessage(message: String?) {
        if (message == null) return
        messageCallback(message)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        closeCallback()
    }

    override fun onError(ex: Exception?) {
        closeCallback()
    }
}
