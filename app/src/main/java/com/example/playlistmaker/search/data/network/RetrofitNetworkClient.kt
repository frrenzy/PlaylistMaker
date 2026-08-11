package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.utils.network.Response

class RetrofitNetworkClient(private val tracksService: ITunesApiService) : NetworkClient {
    override suspend fun searchTracks(dto: TracksSearchRequest): Response {
        try {
            val response = tracksService.search(dto.term)
            return response.apply { resultCode = 200 }
        } catch (_: Throwable) {
            return Response().apply { resultCode = 500 }
        }
    }
}
