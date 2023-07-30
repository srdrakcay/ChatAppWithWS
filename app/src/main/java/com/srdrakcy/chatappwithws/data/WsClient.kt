package com.srdrakcy.chatappwithws.data


import com.srdrakcy.chatappwithws.ui.chatscreen.ChatViewModel
import okhttp3.*
import okio.ByteString
import javax.inject.Inject

class WsClient(
    private val viewModel: ChatViewModel,
    private val okHttpClient: OkHttpClient,
    private val request: Request

) : WebSocketListener() {
    private var webSocket: WebSocket? = null

    fun connectWebSocket() {
        webSocket = okHttpClient.newWebSocket(request, this)
    }

    fun sendMessage(text:String){
        webSocket?.send(text)

    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.triggerEvent(WebSocketResource.Open(response))

    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        viewModel.addMessage(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        viewModel.triggerEvent(WebSocketResource.Closing(reason))
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.triggerEvent(WebSocketResource.Closed(reason))

    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        viewModel.triggerEvent(WebSocketResource.Failure(response))
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        super.onMessage(webSocket, bytes)

    }


}