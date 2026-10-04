package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.sharing.domain.model.EmailData

interface SharingRepository {
    fun getSupportEmailData(): EmailData
    fun getShareAppLink(): String
    fun getTermsLink(): String
    fun getPlaylistData(playlist: Playlist, tracks: List<Track>): String
}
