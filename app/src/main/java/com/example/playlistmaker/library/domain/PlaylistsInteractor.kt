package com.example.playlistmaker.library.domain

import android.net.Uri
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun createPlaylist(playlist: Playlist): Flow<Long>
    fun getPlaylists(): Flow<List<Playlist>>
    fun addTrackToPlaylist(playlistId: Long, track: Track): Flow<CreateResult>
    fun getPlaylistById(playlistId: Long): Flow<Pair<Playlist, List<Track>>>
    suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long)
    suspend fun removePlaylist(playlist: Playlist)
    fun saveCoverImage(uri: Uri, name: String): String?
    fun deleteCoverImage(name: String)
}

sealed interface CreateResult {
    object AlreadyExists : CreateResult
    data class Success(val id: Long) : CreateResult
}
