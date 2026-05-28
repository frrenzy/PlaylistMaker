package com.example.playlistmaker.data

import com.example.playlistmaker.domain.settings.SettingsRepository

class SettingsRepositoryImpl(private val settingsClient: SettingsClient) : SettingsRepository {
    override fun isDark(): Boolean = settingsClient.isDark()

    override fun saveTheme(isDark: Boolean) {
        settingsClient.setTheme(isDark)
    }
}