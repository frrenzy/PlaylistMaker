package com.example.playlistmaker.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playlistmaker.R
import com.example.playlistmaker.common.ui.ScreenLayout
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Settings() {
    val viewModel = koinViewModel<SettingsViewModel>()
    val isDarkThemeEnabled by viewModel.isDark.collectAsState()

    val controlModifier = Modifier.height(24.dp)

    ScreenLayout(title = R.string.settings) {
        Column {
            SettingsLine(title = R.string.settings_theme) {
                Switch(
                    checked = isDarkThemeEnabled,
                    onCheckedChange = { checked -> viewModel.setTheme(checked) },
                    modifier = controlModifier
                )
            }
            SettingsLine(
                title = R.string.settings_share,
                onClick = { viewModel.shareApp() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_share_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = controlModifier,
                )
            }
            SettingsLine(
                title = R.string.settings_support,
                onClick = { viewModel.sendSupportTicket() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_support_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = controlModifier,
                )
            }
            SettingsLine(
                title = R.string.settings_user_agreement,
                onClick = { viewModel.openUserAgreement() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_forward_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = controlModifier,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    Settings()
}
