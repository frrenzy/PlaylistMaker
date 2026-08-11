package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.common.di.appModule
import com.example.playlistmaker.common.di.historyModule
import com.example.playlistmaker.common.di.libraryModule
import com.example.playlistmaker.common.di.playerModule
import com.example.playlistmaker.common.di.searchModule
import com.example.playlistmaker.common.di.settingsModule
import com.example.playlistmaker.common.di.sharingModule
import com.example.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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
