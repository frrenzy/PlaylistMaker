package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import com.example.playlistmaker.library.domain.FavouritesRepository
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(private val repository: FavouritesRepository) :
    FavouriteTracksInteractor {
    override suspend fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override suspend fun removeTrack(track: Track) {
        repository.removeTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> = repository.getTracks()
}
