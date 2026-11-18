package com.example.playlistmaker.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Theme(content: @Composable () -> Unit) {
    val settingsViewModel = koinViewModel<SettingsViewModel>()
    val isDark by settingsViewModel.isDark.collectAsState()

    val colors = when (isDark) {
        true -> darkColors
        false -> lightColors
    }

    MaterialTheme(colorScheme = colors) {
        content()
    }
}
