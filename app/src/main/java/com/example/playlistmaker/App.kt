package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

const val PLAYER_TRACK_KEY = "player_track"

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Creator.provideSettingsInteractor(this).setSavedTheme()
    }
}