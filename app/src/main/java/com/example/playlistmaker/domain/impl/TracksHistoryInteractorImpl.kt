package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.history.TracksHistoryInteractor
import com.example.playlistmaker.domain.history.TracksHistoryRepository
import com.example.playlistmaker.domain.models.Track

class TracksHistoryInteractorImpl(private val repository: TracksHistoryRepository) :
    TracksHistoryInteractor {
    override fun clearHistory() {
        repository.clear()
    }

    override fun saveTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun getSavedTracks(consumer: TracksHistoryInteractor.TracksConsumer) {
        val tracks = repository.loadTracks()
        consumer.consume(tracks)
    }

    override fun isEmpty(): Boolean = repository.isEmpty()
}