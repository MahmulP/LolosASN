package com.lolos.asn.utils

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketClient(private val messageCallback: (String) -> Unit) : WebSocketListener() {
    private val NORMAL_CLOSURE_STATUS = 1000

    fun start() {
        val client = OkHttpClient()

        val request = Request.Builder().url("ws://backend-asn-jchtbiuxra-et.a.run.app").build()
        client.newWebSocket(request, this)
        client.dispatcher.executorService.shutdown()
        Log.d("WebSocketClient", "WebSocket connection started")
    }

    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        webSocket.send("Hello, it's LolosASN!")
        Log.d("WebSocketClient", "WebSocket connection opened")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocketClient", "Receiving : $text")
        messageCallback(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("WebSocketClient", "Receiving bytes : ${bytes.hex()}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        Log.d("WebSocketClient", "Closing : $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        Log.e("WebSocketClient", "Error : ${t.message}")
        t.printStackTrace()
    }
}
