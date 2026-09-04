package com.example.playlistmaker.library.domain

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun createPlaylist(playlist: Playlist): Flow<Long>
    fun getPlaylists(): Flow<List<Playlist>>

    fun addTrackToPlaylist(playlistId: Long, track: Track): Flow<CreateResult>
}

sealed interface CreateResult {
    object AlreadyExists : CreateResult
    data class Success(val id: Long) : CreateResult
}
