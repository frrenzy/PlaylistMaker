package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {
    override var dark = repository.isDark()
        private set

    override fun setTheme(isDark: Boolean) {
        dark = isDark
        repository.saveTheme(isDark)
    }

    override fun setSavedTheme() {
        val isDark = repository.isDark()
        setTheme(isDark)
    }
}