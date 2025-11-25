package dev.cankolay.wakeup.presentation.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.cankolay.wakeup.domain.model.ConnectionState
import dev.cankolay.wakeup.presentation.ui.composable.AppLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaitingScreen(
    state: ConnectionState,
    onDisconnect: () -> Unit,
    onConnect: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onConnect()
    }

    AppLayout(
        title = {
            Text(
                text = when (state) {
                    is ConnectionState.Connected -> "Connected"
                    is ConnectionState.Connecting -> "Connecting..."
                    is ConnectionState.Error -> "Connection Error"
                    is ConnectionState.Disconnected -> "Disconnected"
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            item {
                Text(
                    text = when (state) {
                        is ConnectionState.Connected -> "Listening for signal"
                        is ConnectionState.Connecting -> "Establishing connection..."
                        is ConnectionState.Error -> state.message
                        is ConnectionState.Disconnected -> "Not connected to server"
                    },
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            when (state) {
                is ConnectionState.Connected -> {
                    item {
                        Button(
                            onClick = onDisconnect
                        ) {
                            Text(text = "Disconnect")
                        }
                    }
                }

                is ConnectionState.Disconnected -> {
                    item {
                        Button(
                            onClick = onConnect
                        ) {
                            Text(text = "Connect")
                        }
                    }
                }

                is ConnectionState.Error -> {
                    item {
                        Button(
                            onClick = onConnect
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
