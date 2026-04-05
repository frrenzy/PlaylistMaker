package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val THEME_PREFERENCES_KEY = "theme"
const val PLAYER_TRACK_KEY = "player_track"

class App : Application() {
    var darkTheme = false
    private lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        preferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)

        val darkThemeEnabled = preferences
            .getBoolean(
                THEME_PREFERENCES_KEY,
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
            )
        switchTheme(darkThemeEnabled)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        preferences
            .edit()
            .putBoolean(THEME_PREFERENCES_KEY, darkThemeEnabled)
            .apply()
    }
}