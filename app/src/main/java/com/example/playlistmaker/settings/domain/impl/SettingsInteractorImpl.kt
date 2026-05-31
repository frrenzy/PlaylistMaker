package com.example.playlistmaker.settings.domain.impl

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(
    private val context: Context,
    private val repository: SettingsRepository
) : SettingsInteractor {
    override var dark = repository.isDark()
        private set

    override fun openUserAgreement() {
        val webpage = context.getString(R.string.agreement_link).toUri()
        val viewIntent = Intent(Intent.ACTION_VIEW, webpage)

        context.startActivity(viewIntent)
    }

    override fun sendSupportTicket() {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = "mailto:".toUri()
        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_email)))
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_subject))
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_message))

        context.startActivity(shareIntent)
    }

    override fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            context.getString(R.string.share_message)
        )

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.share_title)
            )
        )
    }

    override fun setTheme(isDark: Boolean) {
        dark = isDark
        repository.saveTheme(isDark)
    }

    override fun setSavedTheme() {
        val isDark = repository.isDark()
        setTheme(isDark)
    }
}