package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.utils.connectBackButton

class SettingsActivity : AppCompatActivity() {
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

        val shareButton = findViewById<LinearLayout>(R.id.settings_share)
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

        val supportButton = findViewById<LinearLayout>(R.id.settings_support)
        supportButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message))

            startActivity(shareIntent)
        }

        val agreementButton = findViewById<LinearLayout>(R.id.settings_agreement)
        agreementButton.setOnClickListener {
            Uri.parse(getString(R.string.agreement_link)).let { webpage ->
                val viewIntent = Intent(Intent.ACTION_VIEW, webpage)

                startActivity(viewIntent)
            }
        }
    }
}