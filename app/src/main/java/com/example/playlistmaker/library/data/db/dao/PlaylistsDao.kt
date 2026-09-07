package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.library.data.db.entities.TrackEntity

@Dao
interface PlaylistsDao {
    @Query("SELECT * FROM playlists")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlists WHERE playlistId = :id")
    suspend fun getPlaylistById(id: Int): PlaylistWithTracks

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createPlaylist(playlistEntity: PlaylistEntity): Long

    @Upsert
    suspend fun createTrack(track: TrackEntity): Long

    @Upsert
    suspend fun createPlaylistTrackCrossRef(ref: PlaylistTrackCrossRefEntity): Long

    @Query("UPDATE playlists SET amount = amount + 1 WHERE playlistId = :playlistId")
    suspend fun updatePlaylistAmount(playlistId: Long)

    @Transaction
    suspend fun addTrackToPlaylist(playlistId: Long, track: TrackEntity): Long {
        createTrack(track)
        val refId =
            createPlaylistTrackCrossRef(PlaylistTrackCrossRefEntity(track.trackId, playlistId))
        if (refId != -1L) {
            updatePlaylistAmount(playlistId)
        }

        return refId
    }
}
