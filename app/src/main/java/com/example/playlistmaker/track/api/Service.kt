package com.example.playlistmaker.track.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApiService {
    @GET("search")
    fun search(
        @Query("term", encoded = true) text: String,
        @Query("entity") entity: String = "song"
    ): Call<SearchTracksResponse>
}

private const val trackServiceBaseUrl = "https://itunes.apple.com/"
private val retrofit = Retrofit
    .Builder()
    .baseUrl(trackServiceBaseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

fun getTracksService(): TrackApiService = retrofit.create(TrackApiService::class.java)
