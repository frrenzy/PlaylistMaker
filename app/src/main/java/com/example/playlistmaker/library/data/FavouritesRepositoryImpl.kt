package com.example.playlistmaker.library.data

import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.data.converters.TrackDbConverter
import com.example.playlistmaker.library.domain.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouritesRepositoryImpl(
    private val db: AppDatabase,
    private val converter: TrackDbConverter
) : FavouritesRepository {
    override suspend fun addTrack(track: Track) {
        val entity = converter.map(track)
        db.favouriteTracksDao().addTrack(entity)
    }

    override suspend fun removeTrack(track: Track) {
        val entity = converter.map(track)
        db.favouriteTracksDao().removeTrack(entity)
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = db.favouriteTracksDao()
            .getTracks()
            .map { converter.map(it) }
            .sortedByDescending { it.isFavourite }
        
        emit(tracks)
    }
}
