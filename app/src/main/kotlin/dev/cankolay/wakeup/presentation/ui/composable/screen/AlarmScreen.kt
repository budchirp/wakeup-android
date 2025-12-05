package dev.cankolay.wakeup.presentation.ui.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.cankolay.wakeup.presentation.ui.composable.AppCard
import dev.cankolay.wakeup.presentation.ui.composable.AppLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    onStopAlarm: () -> Unit,
) {
    AppLayout(
        title = "Alarm"
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            item {
                AppCard {
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
}
