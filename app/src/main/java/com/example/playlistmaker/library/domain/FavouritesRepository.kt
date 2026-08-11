package com.example.playlistmaker.library.domain

import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun getTracks(): Flow<List<Track>>

    //fun getTrackIds(): Flow<List<Long>>
    suspend fun removeTrack(track: Track)
    suspend fun addTrack(track: Track)
}
