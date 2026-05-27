package com.example.playlistmaker.domain.settings

interface SettingsInteractor {
    val dark: Boolean
    fun setSavedTheme()
    fun setTheme(isDark: Boolean)
    fun sendSupportTicket()
    fun openUserAgreement()
    fun shareApp()
}