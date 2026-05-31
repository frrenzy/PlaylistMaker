package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    val dark: Boolean
    fun setSavedTheme()
    fun setTheme(isDark: Boolean)
    fun sendSupportTicket()
    fun openUserAgreement()
    fun shareApp()
}