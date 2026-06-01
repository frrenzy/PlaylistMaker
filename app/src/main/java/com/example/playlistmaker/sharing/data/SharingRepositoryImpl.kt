package com.example.playlistmaker.sharing.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    override fun getShareAppLink(): String = context.getString(R.string.share_message)

    override fun getSupportEmailData(): EmailData = EmailData(
        arrayOf(context.getString(R.string.support_email)),
        context.getString(R.string.support_subject),
        context.getString(R.string.support_message)
    )

    override fun getTermsLink(): String = context.getString(R.string.agreement_link)
}