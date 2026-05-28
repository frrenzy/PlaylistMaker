package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("search")
    fun search(
        @Query("term", encoded = true) term: String,
        @Query("entity") entity: String = "song"
    ): Call<TracksSearchResponse>
}
