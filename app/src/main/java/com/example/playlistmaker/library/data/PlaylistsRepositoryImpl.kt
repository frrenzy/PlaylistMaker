package com.example.playlistmaker.library.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.data.converters.PlaylistDbConverter
import com.example.playlistmaker.library.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.db.DbResult
import com.example.playlistmaker.utils.getCoverImageFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val db: AppDatabase,
    private val converter: PlaylistDbConverter,
    private val context: Context,
) : PlaylistsRepository {
    override fun getPlaylists(): Flow<List<Playlist>> = db.playlistsDao()
        .getPlaylists()
        .map { playlists -> playlists.map { converter.map(it) } }


    override fun addPlaylist(playlist: Playlist) = flow {
        val entity = converter.map(playlist)
        val id = db.playlistsDao().createPlaylist(entity)

        emit(id)
    }

    override fun addTrackToPlaylist(playlistId: Long, track: Track) = flow {
        val entity = converter.map(track)
        when (val id = db.playlistsDao().addTrackToPlaylist(playlistId, entity)) {
            -1L -> emit(DbResult.Conflict)
            else -> emit(DbResult.Success(id))
        }
    }

    override fun getPlaylistById(playlistId: Long): Flow<Pair<Playlist, List<Track>>> =
        db.playlistsDao()
            .getPlaylistById(playlistId)
            .map { data ->
                val playlist = converter.map(data.playlist)
                val tracks = data.tracks.map { converter.map(it) }

                Pair(playlist, tracks)
            }

    override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long) =
        db.playlistsDao().deleteTrackFromPlaylist(trackId, playlistId)

    override suspend fun removePlaylist(playlist: Playlist) {
        val entity = converter.map(playlist)
        db.playlistsDao().deletePlaylist(entity)
    }

    override fun saveCoverImage(uri: Uri, name: String): String? {
        val file = getCoverImageFile(context, name)!!

        context.contentResolver.openInputStream(uri).use { input ->
            FileOutputStream(file).use { output ->
                val decodedStream =
                    BitmapFactory.decodeStream(input) ?: return null
                decodedStream.compress(Bitmap.CompressFormat.JPEG, 30, output)
            }
        }

        return file.name
    }

    override fun deleteCoverImage(name: String) {
        getCoverImageFile(context, name)?.delete()
    }
}
