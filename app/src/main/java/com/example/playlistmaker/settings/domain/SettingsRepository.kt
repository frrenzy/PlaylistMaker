package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun isDark(): Boolean
    fun saveTheme(isDark: Boolean)
}