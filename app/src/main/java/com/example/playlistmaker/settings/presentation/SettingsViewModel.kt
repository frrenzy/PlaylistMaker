package com.example.playlistmaker.settings.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
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

    companion object {
        fun getFactory(context: Context) = viewModelFactory {
            initializer {
                val settingsInteractor = Creator.provideSettingsInteractor(context)
                val sharingInteractor = Creator.provideSharingInteractor(context)
                SettingsViewModel(settingsInteractor, sharingInteractor)
            }
        }
    }
}