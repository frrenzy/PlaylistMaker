package com.example.playlistmaker.history.domain

import com.example.playlistmaker.common.domain.models.Track

interface TracksHistoryInteractor {
    fun clearHistory()
    fun saveTrack(track: Track)
    suspend fun getSavedTracks(): List<Track>
    fun isEmpty(): Boolean
}
