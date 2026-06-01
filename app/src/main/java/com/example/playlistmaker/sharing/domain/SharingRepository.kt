package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.model.EmailData

interface SharingRepository {
    fun getSupportEmailData(): EmailData
    fun getShareAppLink(): String
    fun getTermsLink(): String
}