package com.example.playlistmaker.settings.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {
    private val darkLiveData = MutableLiveData(settingsInteractor.dark)
    fun observeIsDark(): LiveData<Boolean> = darkLiveData

    fun setTheme(isDark: Boolean) {
        settingsInteractor.setTheme(isDark)
        darkLiveData.postValue(isDark)
    }

    fun shareApp(): Unit = settingsInteractor.shareApp()
    fun sendSupportTicket(): Unit = settingsInteractor.sendSupportTicket()
    fun openUserAgreement(): Unit = settingsInteractor.openUserAgreement()

    companion object {
        fun getFactory(context: Context) = viewModelFactory {
            initializer {
                val interactor = Creator.provideSettingsInteractor(context)
                SettingsViewModel(interactor)
            }
        }
    }
}