package com.example.playlistmaker.library.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class CreatePlaylistViewModel(
    protected val playlistsInteractor: PlaylistsInteractor,
) : ViewModel() {
    protected val validityState = MutableLiveData(false)
    fun observeValidity(): LiveData<Boolean> = validityState

    private val message = MutableLiveData<Event<Int>>()
    fun observeMessage(): LiveData<Event<Int>> = message

    protected var name = ""
    protected var description: String? = null
    protected var coverUri: Uri? = null

    fun setName(s: CharSequence?) {
        name = if (s.isNullOrEmpty()) "" else s.toString()
        update()
    }

    fun setDescription(s: CharSequence?) {
        description = s?.toString()
    }

    fun setCoverPath(uri: Uri) {
        coverUri = uri
    }

    fun onCreateClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val playlist = createModel()
                playlistsInteractor.createPlaylist(playlist).collect {
                    message.postValue(Event(R.string.playlist_creation_success))
                    name = ""
                    description = null
                    coverUri = null
                    update()
                }
            }
        }
    }

    private fun update() {
        validityState.postValue(name.isNotEmpty())
    }

    protected open suspend fun createModel(): Playlist {
        val coverImageName = saveImageToPrivateStorage()
        return Playlist(
            name = name,
            description = description,
            coverPath = coverImageName,
        )
    }

    protected fun saveImageToPrivateStorage(): String? = coverUri?.let {
        playlistsInteractor.saveCoverImage(it, name)
    }
}
