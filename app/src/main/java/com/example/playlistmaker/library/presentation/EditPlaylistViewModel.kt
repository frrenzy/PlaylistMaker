package com.example.playlistmaker.library.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.Event
import com.example.playlistmaker.utils.getCoverImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPlaylistViewModel(
    private val playlistId: Long,
    playlistsInteractor: PlaylistsInteractor,
) : CreatePlaylistViewModel(playlistsInteractor) {
    private val playlistEvent = MutableLiveData<Event<Playlist>>()
    fun observePlaylist(): LiveData<Event<Playlist>> = playlistEvent

    private var originalFileName: String? = null
    private var tracksAmount: Int = 0

    fun loadPlaylist() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistsInteractor.getPlaylistById(playlistId).collect {
                    val playlist = it.first
                    playlistEvent.postValue(Event(playlist))

                    name = playlist.name
                    description = playlist.description
                    originalFileName = playlist.coverPath
                    tracksAmount = playlist.amount
                }
            }
        }
    }

    override suspend fun createModel(context: Context): Playlist {
        val coverImage = coverUri?.let { // if new file was picked
            originalFileName?.let { fileName ->
                getCoverImageFile(
                    context,
                    fileName
                )?.delete() // delete old file by its name
            }

            saveImageToPrivateStorage(context) // save new file
        } ?: originalFileName // else keep old

        return Playlist(
            id = playlistId,
            name = name,
            description = description,
            coverPath = coverImage,
            amount = tracksAmount,
        )
    }
}
