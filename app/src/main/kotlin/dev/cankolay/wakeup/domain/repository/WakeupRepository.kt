package dev.cankolay.wakeup.domain.repository

import dev.cankolay.wakeup.domain.model.ConnectionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface WakeupRepository {
    var status: MutableStateFlow<ConnectionState>
    val ringEvents: MutableSharedFlow<Unit>

    suspend fun connect(url: String)
    suspend fun disconnect()
}