package com.example.playlistmaker.library.domain

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.db.DbResult
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylists(): Flow<List<Playlist>>

    fun addPlaylist(playlist: Playlist): Flow<Long>

    fun addTrackToPlaylist(playlistId: Long, track: Track): Flow<DbResult>
}
