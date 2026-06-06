package com.example.playlistmaker.di

import com.example.playlistmaker.settings.data.SettingsClient
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.SharedPrefsSettingsClient
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"

val settingsModule = module {
    single<SettingsClient> {
        SharedPrefsSettingsClient(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
}