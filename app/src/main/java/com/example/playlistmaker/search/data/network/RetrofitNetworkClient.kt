package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.utils.network.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val tracksService: ITunesApiService) : NetworkClient {
    override suspend fun searchTracks(dto: TracksSearchRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = tracksService.search(dto.term)
                response.apply { resultCode = 200 }
            } catch (_: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}
