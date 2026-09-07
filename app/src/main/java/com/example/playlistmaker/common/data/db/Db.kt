package com.example.playlistmaker.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.data.db.dao.FavouriteTracksDao
import com.example.playlistmaker.library.data.db.dao.PlaylistsDao
import com.example.playlistmaker.library.data.db.entities.FavouriteTrackEntity
import com.example.playlistmaker.library.data.db.entities.PlaylistEntity
import com.example.playlistmaker.library.data.db.entities.PlaylistTrackCrossRefEntity
import com.example.playlistmaker.library.data.db.entities.TrackEntity

@Database(
    version = 5,
    entities = [
        FavouriteTrackEntity::class,
        PlaylistEntity::class,
        TrackEntity::class,
        PlaylistTrackCrossRefEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteTracksDao(): FavouriteTracksDao
    abstract fun playlistsDao(): PlaylistsDao
}
