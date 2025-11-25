package dev.cankolay.wakeup.presentation.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.cankolay.wakeup.presentation.ui.composable.AppLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    onConnect: (String) -> Unit
) {
    var url by remember { mutableStateOf(value = "") }

    AppLayout(
        title = {
            Text(text = "Connect to Server")
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            item {
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text(text = "WebSocket URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Button(
                    onClick = { onConnect(url) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = url.isNotBlank()
                ) {
                    Text(text = "Connect")
                }
            }
        }
    }
}

