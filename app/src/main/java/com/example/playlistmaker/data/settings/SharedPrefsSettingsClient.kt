package com.example.playlistmaker.data.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.data.SettingsClient

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