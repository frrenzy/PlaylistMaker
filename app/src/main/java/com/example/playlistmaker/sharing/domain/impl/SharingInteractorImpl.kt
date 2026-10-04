package com.example.playlistmaker.sharing.domain.impl

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingRepository

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val sharingRepository: SharingRepository,
) : SharingInteractor {
    override fun shareApp() {
        val link = sharingRepository.getShareAppLink()
        externalNavigator.shareText(link)
    }

    override fun openTerms() {
        val link = sharingRepository.getTermsLink()
        externalNavigator.openLink(link)
    }

    override fun openSupport() {
        val data = sharingRepository.getSupportEmailData()
        externalNavigator.openEmail(data)
    }

    override fun sharePlaylist(playlist: Playlist, tracks: List<Track>) {
        val data = sharingRepository.getPlaylistData(playlist, tracks)
        externalNavigator.shareText(data)
    }
}
