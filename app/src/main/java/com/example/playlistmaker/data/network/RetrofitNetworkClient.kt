package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val trackServiceBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(trackServiceBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val tracksService = retrofit.create(ITunesApiService::class.java)

    override fun searchTracks(dto: TracksSearchRequest): Response {
        val resp = tracksService.search(dto.term).execute()
        val body = resp.body() ?: Response()
        return body.apply { resultCode = resp.code() }
    }
}