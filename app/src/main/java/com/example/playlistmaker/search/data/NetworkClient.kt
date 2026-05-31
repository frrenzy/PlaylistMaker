package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

interface NetworkClient {
    fun searchTracks(dto: TracksSearchRequest): Response
}