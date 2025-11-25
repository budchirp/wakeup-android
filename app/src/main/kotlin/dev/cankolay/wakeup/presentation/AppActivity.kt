package dev.cankolay.wakeup.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.cankolay.wakeup.presentation.ui.theme.AppTheme
import dev.cankolay.wakeup.presentation.ui.view.WakeupView

@AndroidEntryPoint
class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppTheme {
                WakeupView()
            }
        }
    }
}

