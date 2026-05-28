package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.domain.settings.SettingsInteractor

const val THEME_PREFERENCES_KEY = "theme"
const val PLAYER_TRACK_KEY = "player_track"

class App : Application() {
    var darkTheme = false
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()

        settingsInteractor = Creator.provideSettingsInteractor(this)

        settingsInteractor.setSavedTheme()
    }
}