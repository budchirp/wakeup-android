package dev.cankolay.wakeup.presentation.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.cankolay.wakeup.presentation.ui.composable.AppLayout

@Composable
fun AlarmScreen(
    onStopAlarm: () -> Unit,
) {
    AppLayout(
        title = {
            Text(text = "Alarm")
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Column {
                    Text(
                        text = "Alarm",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = "Wake up!",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                Button(
                    onClick = onStopAlarm
                ) {
                    Text(text = "Stop Alarm")
                }
            }
        }
    }
}
