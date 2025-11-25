package dev.cankolay.wakeup.data.repository

import dev.cankolay.wakeup.data.websocket.WebSocketClient
import dev.cankolay.wakeup.domain.model.ConnectionState
import dev.cankolay.wakeup.domain.repository.WakeupRepository
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WakeupRepositoryImpl @Inject constructor(
    private val client: WebSocketClient
) : WakeupRepository {

    private val scope = CoroutineScope(context = SupervisorJob() + Dispatchers.IO)

    override var status = MutableStateFlow<ConnectionState>(value = ConnectionState.Disconnected)
    override var ringEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private var session: WebSocketSession? = null

    private suspend fun listen() {
        val activeSession = session ?: return

        try {
            for (frame in activeSession.incoming) {
                when (frame) {
                    is Frame.Text -> handle(frame.readText())
                    is Frame.Close -> {
                        status.value = ConnectionState.Disconnected
                        break
                    }

                    else -> Unit
                }
            }
        } catch (e: Exception) {
            status.value = ConnectionState.Error(e.message ?: "Error receiving messages")
        } finally {
            status.value = ConnectionState.Disconnected
        }
    }

    private fun handle(text: String) {
        when (text.trim()) {
            "ring" -> {
                ringEvents.tryEmit(value = Unit)
            }
        }
    }

    override suspend fun connect(url: String) {
        if (status.value is ConnectionState.Connected) return

        status.value = ConnectionState.Connecting

        try {
            session = client.connect(url)
            status.value = ConnectionState.Connected

            scope.launch {
                listen()
            }
        } catch (e: Exception) {
            status.value = ConnectionState.Error(message = e.message ?: "Connection failed")
        }
    }

    override suspend fun disconnect() {
        client.disconnect(session)
        session = null

        status.value = ConnectionState.Disconnected
    }
}


