package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.SettingsRepositoryImpl
import com.example.playlistmaker.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.history.SharedPrefsHistoryClient
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.settings.SharedPrefsSettingsClient
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.history.TracksHistoryInteractor
import com.example.playlistmaker.domain.history.TracksHistoryRepository
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.SettingsRepository

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        val repository = getTracksRepository()
        return TracksInteractorImpl(repository)
    }

    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }

    private fun getHistoryRepository(context: Context): TracksHistoryRepository {
        val sharedPrefs = provideSharedPrefs(context)
        val historyClient = SharedPrefsHistoryClient(sharedPrefs)
        return TracksHistoryRepositoryImpl(historyClient)
    }

    fun provideTracksHistoryInteractor(context: Context): TracksHistoryInteractor {
        val repository = getHistoryRepository(context)
        return TracksHistoryInteractorImpl(repository)
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        val sharedPreferences = provideSharedPrefs(context)
        val settingsClient = SharedPrefsSettingsClient(sharedPreferences)
        return SettingsRepositoryImpl(settingsClient)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val repository = getSettingsRepository(context)
        return SettingsInteractorImpl(context, repository)
    }
}