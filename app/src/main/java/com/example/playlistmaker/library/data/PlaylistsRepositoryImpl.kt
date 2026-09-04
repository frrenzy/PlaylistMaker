package com.example.playlistmaker.library.data

import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.data.converters.PlaylistDbConverter
import com.example.playlistmaker.library.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.db.DbResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val db: AppDatabase,
    private val converter: PlaylistDbConverter,
) : PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = db.playlistsDao()
            .getPlaylists()
            .map { converter.map(it) }

        emit(playlists)
    }

    override fun addPlaylist(playlist: Playlist) = flow {
        val entity = converter.map(playlist)
        val id = db.playlistsDao().createPlaylist(entity)

        emit(id)
    }

    override fun addTrackToPlaylist(playlistId: Long, track: Track) = flow {
        val entity = converter.map(track)
        when (val id = db.playlistsDao().addTrackToPlaylist(playlistId, entity)) {
            -1L -> emit(DbResult.Conflict)
            else -> emit(DbResult.Success(id))
        }
    }
}
