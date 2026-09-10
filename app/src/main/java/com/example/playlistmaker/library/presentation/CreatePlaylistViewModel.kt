package com.example.playlistmaker.library.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.Event
import com.example.playlistmaker.utils.getCoverImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

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

    fun onCreateClick(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val playlist = createModel(context)
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

    protected open suspend fun createModel(context: Context): Playlist {
        val coverImageName = saveImageToPrivateStorage(context)
        return Playlist(
            name = name,
            description = description,
            coverPath = coverImageName,
        )
    }

    protected fun saveImageToPrivateStorage(context: Context): String? = coverUri?.let {
        val file = getCoverImageFile(context, "${name.trim()}.jpg")!!

        context.contentResolver.openInputStream(it).use { input ->
            FileOutputStream(file).use { output ->
                val decodedStream =
                    BitmapFactory.decodeStream(input) ?: return null
                decodedStream.compress(Bitmap.CompressFormat.JPEG, 30, output)
            }
        }

        return file.name
    }
}
