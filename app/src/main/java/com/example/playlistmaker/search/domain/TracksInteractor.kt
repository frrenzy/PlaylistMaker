package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun searchTracks(term: String, consumer: TracksConsumer)

    fun interface TracksConsumer {
        fun consume(result: SearchResult)
    }
}

sealed interface SearchResult {
    data class Success(val tracks: List<Track>) : SearchResult
    object Empty : SearchResult
    data class Error(val message: String) : SearchResult
}