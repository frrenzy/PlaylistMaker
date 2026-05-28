package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.utils.connectBackButton

class SettingsActivity : AppCompatActivity() {
    private lateinit var themeSwitcher: Switch
    private lateinit var shareButton: LinearLayout
    private lateinit var supportButton: LinearLayout
    private lateinit var agreementButton: LinearLayout

    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        connectBackButton(R.id.settings_back_button)

        settingsInteractor = Creator.provideSettingsInteractor(this)

        themeSwitcher = findViewById(R.id.settings_theme_switcher)
        themeSwitcher.isChecked = settingsInteractor.dark
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsInteractor.setTheme(checked)
        }

        shareButton = findViewById(R.id.settings_share)
        shareButton.setOnClickListener {
            settingsInteractor.shareApp()
        }

        supportButton = findViewById(R.id.settings_support)
        supportButton.setOnClickListener {
            settingsInteractor.sendSupportTicket()
        }

        agreementButton = findViewById(R.id.settings_agreement)
        agreementButton.setOnClickListener {
            settingsInteractor.openUserAgreement()
        }
    }
}