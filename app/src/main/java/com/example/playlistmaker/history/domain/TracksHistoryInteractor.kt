package com.example.playlistmaker.history.domain

import com.example.playlistmaker.search.domain.models.Track

interface TracksHistoryInteractor {
    fun clearHistory()
    fun saveTrack(track: Track)
    fun getSavedTracks(): List<Track>
    fun isEmpty(): Boolean
}