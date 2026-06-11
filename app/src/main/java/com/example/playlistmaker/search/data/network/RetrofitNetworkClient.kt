package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(private val tracksService: ITunesApiService) : NetworkClient {
    override fun searchTracks(dto: TracksSearchRequest): Response {
        val resp = tracksService.search(dto.term).execute()
        val body = resp.body() ?: Response()
        return body.apply { resultCode = resp.code() }
    }
}