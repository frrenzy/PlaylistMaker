package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.history.data.SharedPrefsHistoryClient
import com.example.playlistmaker.history.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.history.domain.TracksHistoryInteractor
import com.example.playlistmaker.history.domain.TracksHistoryRepository
import com.example.playlistmaker.history.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.data.SharedPrefsSettingsClient
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl

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