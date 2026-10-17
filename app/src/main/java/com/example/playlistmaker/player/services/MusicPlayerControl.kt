package com.example.playlistmaker.player.services

import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.flow.StateFlow

interface MusicPlayerControl {
    fun getPlayerState(): StateFlow<MusicPlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun startForeground()
    fun stopForeground()
}

sealed class MusicPlayerState(val interactible: Boolean, val progressTime: String) {
    object Default : MusicPlayerState(false, Track.DEFAULT_TRACK_TIME)
    object Prepared : MusicPlayerState(true, Track.DEFAULT_TRACK_TIME)
    class Playing(progress: String) : MusicPlayerState(true, progress)
    class Paused(progress: String) : MusicPlayerState(true, progress)
}
