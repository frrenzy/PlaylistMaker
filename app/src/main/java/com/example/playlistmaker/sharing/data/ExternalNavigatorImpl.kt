package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun openEmail(data: EmailData) {
        val shareIntent = Intent(Intent.ACTION_SENDTO)
        shareIntent.data = "mailto:".toUri()
        data.apply {
            shareIntent.putExtra(Intent.EXTRA_EMAIL, addresses)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        }

        context.startActivity(shareIntent)
    }

    override fun openLink(link: String) {
        val webpage = link.toUri()
        val viewIntent = Intent(Intent.ACTION_VIEW, webpage)

        context.startActivity(viewIntent)
    }

    override fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.share_title)
            )
        )
    }
}