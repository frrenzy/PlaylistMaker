package com.example.playlistmaker.data

interface SettingsClient {
    fun isDark(): Boolean
    fun setTheme(isDark: Boolean)
}