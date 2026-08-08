package com.example.playlistmaker.history.domain.impl

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.history.domain.TracksHistoryInteractor
import com.example.playlistmaker.history.domain.TracksHistoryRepository

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository) :
    TracksHistoryInteractor {
    override fun clearHistory() = repository.clear()

    override fun saveTrack(track: Track) = repository.addTrack(track)

    override suspend fun getSavedTracks(): List<Track> = repository.loadTracks()

    override fun isEmpty(): Boolean = repository.isEmpty()
}
