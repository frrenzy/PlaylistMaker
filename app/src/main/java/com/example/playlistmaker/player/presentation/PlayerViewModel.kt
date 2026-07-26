package com.example.playlistmaker.player.presentation

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PlayerViewModel(track: Track) : ViewModel() {
    private val player = MediaPlayer()

    private var updateTimer: Job? = null

    private val trackLiveData = MutableLiveData(track)
    fun observeTrack(): LiveData<Track> = trackLiveData

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    init {
        preparePlayer(track.previewUrl)
    }

    fun onPlayButtonClick() {
        when (playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> Unit
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
