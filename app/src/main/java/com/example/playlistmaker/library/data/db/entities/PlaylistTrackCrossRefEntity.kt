package com.example.playlistmaker.library.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "playlist_tracks_junction",
    primaryKeys = ["trackId", "playlistId"],
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["playlistId"],
        childColumns = ["playlistId"],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class PlaylistTrackCrossRefEntity(
    val trackId: Long,
    val playlistId: Long,
)
