package com.example.playlistmaker.search.domain

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.utils.network.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(term: String): Flow<Resource<List<Track>>>
}
