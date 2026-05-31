package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class SharedPrefsSettingsClient(val preferences: SharedPreferences) : SettingsClient {
    override fun isDark(): Boolean = preferences
        .getBoolean(
            THEME_PREFERENCES_KEY,
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        )

    override fun setTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        preferences.edit {
            putBoolean(THEME_PREFERENCES_KEY, isDark)
        }
    }

    companion object {
        private const val THEME_PREFERENCES_KEY = "theme"
    }
}