package com.example.playlistmaker.settings.data

interface SettingsClient {
    fun isDark(): Boolean
    fun setTheme(isDark: Boolean)
}