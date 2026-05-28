package com.example.playlistmaker.domain.history

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryRepository {
    fun addTrack(track: Track)
    fun loadTracks(): List<Track>
    fun clear()
    fun isEmpty(): Boolean
}