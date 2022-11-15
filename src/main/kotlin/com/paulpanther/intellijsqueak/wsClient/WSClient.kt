package com.paulpanther.intellijsqueak.wsClient

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class WSClient(
    private val onReceive: (msg: String) -> Unit,
    private val onClose: () -> Unit
): WebSocketClient(URI("localhost:2424")) {

    override fun onOpen(handshake: ServerHandshake?) {}

    override fun onMessage(message: String?) {
        if (message == null) return
        onReceive(message)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        onClose()
    }

    override fun onError(ex: Exception?) {
        onClose()
    }
}