package com.srdrakcy.chatappwithws.data

import okhttp3.Response

sealed class WebSocketResource {
    data class Open(val response: Response):WebSocketResource()
    data class Closed(val reason: String) : WebSocketResource()
    data class Closing(val reason: String) : WebSocketResource()
    data class Failure(val response: Response?) : WebSocketResource()
}
