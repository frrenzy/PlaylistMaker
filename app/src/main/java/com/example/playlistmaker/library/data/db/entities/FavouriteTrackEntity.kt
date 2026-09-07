package com.example.playlistmaker.library.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_tracks")
data class FavouriteTrackEntity(
    @PrimaryKey @ColumnInfo(name = "id")
    val trackId: Long,
    @ColumnInfo(name = "name")
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String?,
    val coverArtworkUrl: String?,
    @ColumnInfo(name = "time")
    val trackTimeMillis: Int,
    val collectionName: String?,
    val country: String,
    val previewUrl: String?,
    val primaryGenreName: String,
    val releaseDate: String?,
)
