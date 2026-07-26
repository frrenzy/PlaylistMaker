package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.SearchResult
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.utils.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(term: String): Flow<SearchResult> =
        repository.searchTracks(term).map {
            when (it) {
                is Resource.Error -> SearchResult.Error(it.message ?: "Ошибка сервера")
                is Resource.Success -> {
                    if (it.data.isNullOrEmpty()) {
                        SearchResult.Empty
                    } else SearchResult.Success(it.data)
                }
            }
        }
}
