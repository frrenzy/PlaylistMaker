package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.appModule
import com.example.playlistmaker.di.historyModule
import com.example.playlistmaker.di.libraryModule
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingsModule
import com.example.playlistmaker.di.sharingModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val PLAYER_TRACK_KEY = "player_track"

class App : Application() {
    private val settingsInteractor: SettingsInteractor by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                playerModule,
                sharingModule,
                settingsModule,
                historyModule,
                searchModule,
                libraryModule,
            )
        }

        settingsInteractor.setSavedTheme()
    }
}
