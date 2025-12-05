package dev.cankolay.wakeup.presentation.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dev.cankolay.wakeup.domain.model.VibrationIntensity
import dev.cankolay.wakeup.presentation.ui.composable.AppCard
import dev.cankolay.wakeup.presentation.ui.composable.AppLayout
import dev.cankolay.wakeup.presentation.ui.composable.AppTopAppBar
import dev.cankolay.wakeup.presentation.ui.composition.LocalNavController
import dev.cankolay.wakeup.presentation.ui.viewmodel.WakeupViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    viewModel: WakeupViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val url by viewModel.url.collectAsState()
    val volume by viewModel.volume.collectAsState()
    val vibrationIntensity by viewModel.vibrationIntensity.collectAsState()
    val alarmDuration by viewModel.alarmDuration.collectAsState()
    val testing by viewModel.testing.collectAsState()

    AppLayout(title = "Settings", topBar = { title, scrollBehavior ->
        AppTopAppBar(
            title = title,
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
    }) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            item {
                AppCard {
                    Text(text = "Server")

                    OutlinedTextField(
                        value = url ?: "",
                        onValueChange = { viewModel.updateUrl(value = it) },
                        label = { Text(text = "WebSocket URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            item {
                AppCard {
                    Text(text = "Vibration Intensity")

                    Column(
                        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
                    ) {
                        VibrationIntensity.entries.forEach { intensity ->
                            val selected = vibrationIntensity == intensity

                            ClickableRow(selected = selected, onClick = {
                                viewModel.updateVibrationIntensity(
                                    intensity = intensity
                                )
                            }, horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = intensity.label,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                if (selected) {
                                    Icon(
                                        modifier = Modifier
                                            .size(size = 24.dp)
                                            .requiredSize(size = 24.dp),
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }



            item {
                AppCard {
                    Text(text = "Alarm Volume")

                    Column {
                        Text(
                            text = "${(volume * 100).roundToInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Slider(
                            value = volume,
                            onValueChange = { viewModel.updateVolume(value = it) },
                            valueRange = 0f..1f,
                            steps = (100 / 5) - 1,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                AppCard {
                    Text(text = "Alarm Duration")

                    Column(
                        verticalArrangement = Arrangement.spacedBy(space = 4.dp)
                    ) {
                        val isInfinite = alarmDuration == -1

                        ClickableRow(selected = isInfinite, onClick = {
                            viewModel.updateAlarmDuration(seconds = -1)
                        }, horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                            RadioButton(
                                modifier = Modifier.size(size = 24.dp),
                                selected = isInfinite,
                                onClick = { viewModel.updateAlarmDuration(seconds = -1) }
                            )

                            Text(
                                text = "Infinite",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        ClickableRow(
                            selected = !isInfinite,
                            onClick = {
                                viewModel.updateAlarmDuration(seconds = 30)
                            }, horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            RadioButton(
                                modifier = Modifier.size(size = 24.dp),
                                selected = !isInfinite,
                                onClick = {
                                    viewModel.updateAlarmDuration(seconds = 30)
                                }
                            )

                            Column {
                                Text(
                                    text = "Custom duration",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                AnimatedVisibility(
                                    visible = !isInfinite,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column {
                                        Text(
                                            text = "${alarmDuration}s",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        Slider(
                                            value = alarmDuration.toFloat(),
                                            onValueChange = {
                                                viewModel.updateAlarmDuration(
                                                    seconds = it.roundToInt()
                                                )
                                            },
                                            valueRange = 3f..60f,
                                            steps = (60 - 3) / 3 - 1,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                if (testing) {
                    Button(
                        onClick = { viewModel.test() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Stop Test")
                    }
                } else {
                    OutlinedButton(
                        onClick = { viewModel.test() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Test Alarm")
                    }
                }
            }
        }
    }
}

@Composable
fun ClickableRow(
    selected: Boolean,
    onClick: () -> Unit,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = if (selected) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.extraLarge)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
        ) {
            content()
        }
    }
}