package com.example.playlistmaker.settings.data

import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val settingsClient: SettingsClient) : SettingsRepository {
    override fun isDark(): Boolean = settingsClient.isDark()

    override fun saveTheme(isDark: Boolean) {
        settingsClient.setTheme(isDark)
    }
}