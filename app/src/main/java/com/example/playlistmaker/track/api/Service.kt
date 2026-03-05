package com.example.playlistmaker.track.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApiService {
    @GET("search")
    fun search(
        @Query("term") text: String,
        @Query("entity") entity: String = "song"
    ): Call<SearchTracksResponse>
}
