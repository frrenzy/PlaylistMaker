package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.FavouriteTrackEntity

@Dao
interface FavouriteTracksDao {
    @Query("SELECT * FROM favourite_tracks")
    suspend fun getTracks(): List<FavouriteTrackEntity>

    @Query("SELECT id FROM favourite_tracks")
    suspend fun getTrackIds(): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: FavouriteTrackEntity)

    @Delete
    suspend fun removeTrack(track: FavouriteTrackEntity)
}
