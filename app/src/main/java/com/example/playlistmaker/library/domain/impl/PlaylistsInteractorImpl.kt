package com.example.playlistmaker.library.domain.impl

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.CreateResult
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.db.DbResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository
) : PlaylistsInteractor {
    override fun getPlaylists() = repository.getPlaylists()

    override fun createPlaylist(playlist: Playlist) = repository.addPlaylist(playlist)

    override fun addTrackToPlaylist(playlistId: Long, track: Track): Flow<CreateResult> =
        repository.addTrackToPlaylist(playlistId, track).map {
            when (it) {
                is DbResult.Conflict -> CreateResult.AlreadyExists
                is DbResult.Success -> CreateResult.Success(it.id)
            }
        }
}
