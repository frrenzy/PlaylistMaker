package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingRepository

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val sharingRepository: SharingRepository,
) : SharingInteractor {
    override fun shareApp() {
        val link = sharingRepository.getShareAppLink()
        externalNavigator.shareLink(link)
    }

    override fun openTerms() {
        val link = sharingRepository.getTermsLink()
        externalNavigator.openLink(link)
    }

    override fun openSupport() {
        val data = sharingRepository.getSupportEmailData()
        externalNavigator.openEmail(data)
    }
}