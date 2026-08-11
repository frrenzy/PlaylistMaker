package com.example.playlistmaker.history.domain

import com.example.playlistmaker.common.domain.models.Track

interface TracksHistoryRepository {
    fun addTrack(track: Track)
    suspend fun loadTracks(): List<Track>
    fun clear()
    fun isEmpty(): Boolean
}
