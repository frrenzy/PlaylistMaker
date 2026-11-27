package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val _isDark = MutableStateFlow(settingsInteractor.dark)
    val isDark = _isDark.asStateFlow()

    fun setTheme(isDarkSelected: Boolean) {
        settingsInteractor.setTheme(isDarkSelected)
        _isDark.value = isDarkSelected
    }

    fun shareApp(): Unit = sharingInteractor.shareApp()
    fun sendSupportTicket(): Unit = sharingInteractor.openSupport()
    fun openUserAgreement(): Unit = sharingInteractor.openTerms()
}
