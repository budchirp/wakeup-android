package dev.cankolay.wakeup.presentation.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.cankolay.wakeup.domain.model.ConnectionState
import dev.cankolay.wakeup.presentation.ui.composable.AppLayout
import dev.cankolay.wakeup.presentation.ui.composable.AppTopAppBar
import dev.cankolay.wakeup.presentation.ui.composition.LocalNavController
import dev.cankolay.wakeup.presentation.ui.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaitingScreen(
    state: ConnectionState,
    onDisconnect: () -> Unit,
    onConnect: () -> Unit
) {
    val navController = LocalNavController.current

    LaunchedEffect(key1 = Unit) {
        onConnect()
    }

    AppLayout(
        title = when (state) {
            is ConnectionState.Connected -> "Connected"
            is ConnectionState.Connecting -> "Connecting..."
            is ConnectionState.Error -> "Connection Error"
            is ConnectionState.Disconnected -> "Disconnected"
        },
        topBar = { title, scrollBehavior ->
            AppTopAppBar(
                title = title,
                actions = {
                    IconButton(onClick = { navController.navigate(route = Route.Settings) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
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
