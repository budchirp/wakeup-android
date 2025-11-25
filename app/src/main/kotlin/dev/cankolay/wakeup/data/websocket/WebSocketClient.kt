package dev.cankolay.wakeup.data.websocket

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor() {

    private val httpClient = HttpClient(engineFactory = OkHttp) {
        install(plugin = WebSockets)
    }

    suspend fun connect(url: String): WebSocketSession =
        httpClient.webSocketSession {
            url(url)
        }

    suspend fun disconnect(session: WebSocketSession?) {
        session?.close()
    }
}


