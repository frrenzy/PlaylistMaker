package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest

interface NetworkClient {
    fun searchTracks(dto: TracksSearchRequest): Response
}