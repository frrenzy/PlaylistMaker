package com.example.playlistmaker.library.presentation

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.common.data.Constants
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.utils.Event
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistViewModel(
    app: Application,
    private val playlistsInteractor: PlaylistsInteractor
) : AndroidViewModel(app) {
    private val validityState = MutableLiveData(false)
    fun observeValidity(): LiveData<Boolean> = validityState

    private val message = MutableLiveData<Event<String>>()
    fun observeMessage(): LiveData<Event<String>> = message

    private var name = ""
    private var description = ""
    private var coverPath: Uri? = null

    fun setName(s: CharSequence?) {
        name = if (s.isNullOrEmpty()) "" else s.toString()
        update()
    }

    fun setDescription(s: CharSequence?) {
        description = if (s.isNullOrEmpty()) "" else s.toString()
    }

    fun setCoverPath(uri: Uri) {
        coverPath = uri
    }

    fun onCreateClick() {
        viewModelScope.launch {
            val coverImageName = saveImageToPrivateStorage(coverPath, name)
            playlistsInteractor.createPlaylist(
                Playlist(
                    name = name,
                    description = description,
                    coverPath = coverImageName,
                )
            ).collect {
                message.postValue(
                    Event(
                        getApplication<App>()
                            .getString(R.string.playlist_creation_success)
                    )
                )
            }
        }
    }

    private fun update() {
        validityState.postValue(name.isNotEmpty())
    }

    private fun saveImageToPrivateStorage(uri: Uri?, name: String): String? {
        if (uri == null) return null

        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath =
            File(
                getApplication<App>().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                Constants.COVERS_DIR,
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "${name.trim()}.jpg")
        val inputStream = getApplication<App>().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.name
    }
}
