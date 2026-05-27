package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.connectBackButton

class SettingsActivity : AppCompatActivity() {
    private lateinit var themeSwitcher: Switch
    private lateinit var shareButton: LinearLayout
    private lateinit var supportButton: LinearLayout
    private lateinit var agreementButton: LinearLayout

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

        themeSwitcher = findViewById(R.id.settings_theme_switcher)
        themeSwitcher.isChecked = (applicationContext as App).darkTheme
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        shareButton = findViewById(R.id.settings_share)
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.share_message)
            )

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.share_title)
                )
            )
        }

        supportButton = findViewById(R.id.settings_support)
        supportButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = "mailto:".toUri()
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))

            startActivity(shareIntent)
        }

        agreementButton = findViewById(R.id.settings_agreement)
        agreementButton.setOnClickListener {
            val webpage = getString(R.string.agreement_link).toUri()
            val viewIntent = Intent(Intent.ACTION_VIEW, webpage)

            startActivity(viewIntent)
        }
    }
}