package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun openEmail(email: EmailData) {
        val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, email.addresses)
            putExtra(Intent.EXTRA_SUBJECT, email.subject)
            putExtra(Intent.EXTRA_TEXT, email.message)

            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(shareIntent)
    }

    override fun openLink(link: String) {
        val webpage = link.toUri()
        val viewIntent = Intent(Intent.ACTION_VIEW, webpage).apply {
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(viewIntent)
    }

    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            setType("text/plain")
            putExtra(Intent.EXTRA_TEXT, link)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val chooserIntent =
            Intent.createChooser(shareIntent, context.getString(R.string.share_title)).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

        context.startActivity(chooserIntent)
    }
}