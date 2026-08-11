package com.example.playlistmaker.player.presentation

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.data.db.AppDatabase
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(
    track: Track,
    private val favouritesInteractor: FavouriteTracksInteractor,
    private val db: AppDatabase,
) : ViewModel() {
    private val player = MediaPlayer()

    private var updateTimer: Job? = null

    private val trackLiveData = MutableLiveData<Track>()
    fun observeTrack(): LiveData<Track> = trackLiveData

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    init {
        preparePlayer(track.previewUrl)
        viewModelScope.launch {
            val favouriteTrackIds = db.favouriteTracksDao().getTrackIds()
            val isTrackInFavourite = favouriteTrackIds.contains(track.trackId)

            trackLiveData.postValue(track.copy(isFavourite = isTrackInFavourite))
        }
    }

    fun onPlayButtonClick() {
        when (playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> Unit
        }
    }

    fun onLikeButtonClick() {
        val track = trackLiveData.value ?: return

        viewModelScope.launch {
            if (track.isFavourite) {
                favouritesInteractor.removeTrack(track)
                trackLiveData.postValue(track.copy(isFavourite = false))
            } else {
                favouritesInteractor.addTrack(track)
                trackLiveData.postValue(track.copy(isFavourite = true))
            }
        }
    }

    private fun startPlayer() {
        player.start()
        playerStateLiveData.postValue(PlayerState.Playing(formatTime(player.currentPosition)))
        startTimer()
    }

    private fun pausePlayer() {
        player.pause()
        stopTimer()
        playerStateLiveData.postValue(PlayerState.Paused(formatTime(player.currentPosition)))
    }

    private fun preparePlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerStateLiveData.postValue(PlayerState.Prepared)
        }
        player.setOnCompletionListener {
            stopTimer()
            playerStateLiveData.postValue(PlayerState.Prepared)
        }
    }

    private fun startTimer() {
        updateTimer?.cancel()
        updateTimer = viewModelScope.launch {
            while (player.isPlaying) {
                delay(TRACK_TIME_UPDATE_INTERVAL)
                playerStateLiveData.postValue(PlayerState.Playing(formatTime(player.currentPosition)))
            }
        }
    }

    private fun stopTimer() {
        updateTimer?.cancel()
    }

    fun onPaused() {
        pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        pausePlayer()
        player.release()
    }

    private fun formatTime(time: Int): String = Track.trackTimeFormat.format(time)

    companion object {
        private val TRACK_TIME_UPDATE_INTERVAL = 300.milliseconds
    }

}

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val progressTime: String) {
    object Default : PlayerState(false, Track.DEFAULT_TRACK_TIME)
    object Prepared : PlayerState(true, Track.DEFAULT_TRACK_TIME)
    class Playing(progress: String) : PlayerState(true, progress)
    class Paused(progress: String) : PlayerState(true, progress)
}
