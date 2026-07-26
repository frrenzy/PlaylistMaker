package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(term: String): Flow<SearchResult>
}

sealed interface SearchResult {
    data class Success(val tracks: List<Track>) : SearchResult
    object Empty : SearchResult
    data class Error(val message: String) : SearchResult
}
