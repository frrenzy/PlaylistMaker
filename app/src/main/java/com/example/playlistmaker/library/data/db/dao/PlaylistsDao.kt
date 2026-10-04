package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.library.data.db.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlaylistsDao {
    @Query("SELECT * FROM playlists")
    abstract fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Transaction
    @Query(
        """
        SELECT *
        FROM playlists 
        WHERE playlistId = :id
        """
    )
    abstract fun getPlaylistById(id: Long): Flow<PlaylistWithTracks>

    @Upsert
    abstract fun createPlaylist(playlistEntity: PlaylistEntity): Long

    @Upsert
    internal abstract suspend fun createTrack(track: TrackEntity): Long

    @Upsert
    internal abstract suspend fun createPlaylistTrackCrossRef(ref: PlaylistTrackCrossRefEntity): Long

    @Query(
        """
        UPDATE playlists
        SET amount = amount + 1
        WHERE playlistId = :playlistId
        """
    )
    internal abstract suspend fun increasePlaylistAmount(playlistId: Long)

    @Query(
        """
        UPDATE playlists 
        SET amount = amount - 1
        WHERE playlistId = :playlistId
        """
    )
    internal abstract suspend fun decreasePlaylistAmount(playlistId: Long)

    @Transaction
    open suspend fun addTrackToPlaylist(playlistId: Long, track: TrackEntity): Long {
        createTrack(track)
        val refId =
            createPlaylistTrackCrossRef(PlaylistTrackCrossRefEntity(track.trackId, playlistId))
        if (refId != -1L) {
            increasePlaylistAmount(playlistId)
        }

        return refId
    }

    @Query("DELETE FROM tracks WHERE trackId = :trackId")
    internal abstract suspend fun deleteTrackById(trackId: Long)

    @Delete
    internal abstract suspend fun deletePlaylistTrackCrossRef(ref: PlaylistTrackCrossRefEntity)

    @Query(
        """
        SELECT t.trackId 
        FROM tracks t 
        LEFT JOIN playlist_tracks_junction j 
            ON t.trackId = j.trackId 
        WHERE j.trackId IS NULL
        """
    )
    internal abstract suspend fun getRedundantTracks(): List<Long>

    @Query("DELETE FROM tracks WHERE trackId IN (:trackIds)")
    internal abstract suspend fun deleteTracksById(trackIds: List<Long>)

    internal suspend fun deleteRedundantTracks() {
        val idsToDelete = getRedundantTracks()
        deleteTracksById(idsToDelete)
    }

    @Transaction
    open suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        val crossRef = PlaylistTrackCrossRefEntity(trackId, playlistId)
        deletePlaylistTrackCrossRef(crossRef)
        decreasePlaylistAmount(playlistId)

        deleteRedundantTracks()
    }

    @Delete
    internal abstract suspend fun deletePlaylistEntity(playlist: PlaylistEntity)

    @Transaction
    open suspend fun deletePlaylist(playlist: PlaylistEntity) {
        deletePlaylistEntity(playlist)

        deleteRedundantTracks()
    }
}
