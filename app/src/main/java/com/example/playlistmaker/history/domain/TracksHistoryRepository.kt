package com.example.playlistmaker.history.domain

import com.example.playlistmaker.search.domain.models.Track

interface TracksHistoryRepository {
    fun addTrack(track: Track)
    fun loadTracks(): List<Track>
    fun clear()
    fun isEmpty(): Boolean
}