package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareText(text: String)
    fun openLink(link: String)
    fun openEmail(data: EmailData)
}
