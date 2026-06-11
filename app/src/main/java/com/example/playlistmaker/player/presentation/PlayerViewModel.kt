package com.example.playlistmaker.player.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track

class PlayerViewModel(track: Track) : ViewModel() {
    private val player = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private val updateTrackTimeRunnable: Runnable = Runnable {
        player.currentPosition.let {
            playerStateLiveData.postValue(
                PlayerState(
                    playerStateLiveData.value?.state ?: MediaState.DEFAULT,
                    formatTime(it),
                    true
                )
            )
        }
        handler.postDelayed(updateTrackTimeRunnable, TRACK_TIME_UPDATE_INTERVAL)
    }

    private val trackLiveData = MutableLiveData(track)
    fun observeTrack(): LiveData<Track> = trackLiveData

    private val playerStateLiveData =
        MutableLiveData(
            PlayerState(
                MediaState.DEFAULT,
                formatTime(0),
                false
            )
        )

    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    init {
        preparePlayer(track.previewUrl)
    }

    fun onPlayButtonClick() {
        val playerState = playerStateLiveData.value?.state ?: MediaState.DEFAULT
        when (playerState) {
            MediaState.PLAYING -> pausePlayer()
            MediaState.PREPARED, MediaState.PAUSED -> startPlayer()
            else -> Unit
        }
    }

    private fun startPlayer() {
        player.start()
        handler.postDelayed(updateTrackTimeRunnable, TRACK_TIME_UPDATE_INTERVAL)
        playerStateLiveData.postValue(
            PlayerState(
                MediaState.PLAYING,
                formatTime(0),
                true,
            )
        )
    }

    private fun pausePlayer() {
        player.pause()
        handler.removeCallbacks(updateTrackTimeRunnable)
        playerStateLiveData.postValue(
            PlayerState(
                MediaState.PAUSED,
                formatTime(0),
                true,
            )
        )
    }

    private fun preparePlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerStateLiveData.postValue(
                PlayerState(
                    MediaState.PREPARED,
                    formatTime(0),
                    true,
                )
            )
        }
        player.setOnCompletionListener {
            handler.removeCallbacks(updateTrackTimeRunnable)
            playerStateLiveData.postValue(
                PlayerState(
                    MediaState.PREPARED,
                    formatTime(0),
                    true,
                )
            )
        }
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

    enum class MediaState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED,
    }

    companion object {
        data class PlayerState(
            val state: MediaState,
            val progressTime: String,
            val isPlayButtonEnabled: Boolean,
        )

        private const val TRACK_TIME_UPDATE_INTERVAL = 300L
    }
}