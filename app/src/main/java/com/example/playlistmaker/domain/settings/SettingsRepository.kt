package com.example.playlistmaker.domain.settings

interface SettingsRepository {
    fun isDark(): Boolean
    fun saveTheme(isDark: Boolean)
}