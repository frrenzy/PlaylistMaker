package com.example.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val darkLiveData = MutableLiveData(settingsInteractor.dark)
    fun observeIsDark(): LiveData<Boolean> = darkLiveData

    fun setTheme(isDark: Boolean) {
        settingsInteractor.setTheme(isDark)
        darkLiveData.postValue(isDark)
    }

    fun shareApp(): Unit = sharingInteractor.shareApp()
    fun sendSupportTicket(): Unit = sharingInteractor.openSupport()
    fun openUserAgreement(): Unit = sharingInteractor.openTerms()
}