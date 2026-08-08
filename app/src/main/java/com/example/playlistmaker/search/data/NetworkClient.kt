package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.utils.network.Response

interface NetworkClient {
    suspend fun searchTracks(dto: TracksSearchRequest): Response
}
