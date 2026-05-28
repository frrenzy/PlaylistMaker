package com.example.playlistmaker.domain.history

import com.example.playlistmaker.domain.models.Track

interface TracksHistoryInteractor {
    fun clearHistory()
    fun saveTrack(track: Track)
    fun getSavedTracks(consumer: TracksConsumer)
    fun isEmpty(): Boolean

    fun interface TracksConsumer {
        fun consume(result: List<Track>)
    }
}