package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entities.FavouriteTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteTracksDao {
    @Query("SELECT * FROM favourite_tracks")
    fun getTracks(): Flow<List<FavouriteTrackEntity>>

    @Query("SELECT id FROM favourite_tracks")
    fun getTrackIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: FavouriteTrackEntity)

    @Delete
    suspend fun removeTrack(track: FavouriteTrackEntity)
}
