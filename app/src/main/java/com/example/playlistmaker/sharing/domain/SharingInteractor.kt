package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.models.Playlist

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlaylist(playlist: Playlist, tracks: List<Track>)
}
