package com.example.playlistmaker.player.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.common.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MusicService : Service(), MusicPlayerControl {
    private var track: Track? = null
    private var player: MediaPlayer? = null

    private var isForegroundRunning = false

    private var updateTimerJob: Job? = null

    private val _state = MutableStateFlow<MusicPlayerState>(MusicPlayerState.Default)
    val state = _state.asStateFlow()

    private val binder = MusicServiceBinder()

    override fun onBind(intent: Intent?): IBinder {
        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(PLAYER_TRACK_KEY, Track::class.java)
        } else {
            intent?.getParcelableExtra(PLAYER_TRACK_KEY)
        }
        createNotificationChannel()
        initPlayer()
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()

        releasePlayer()
    }

    override fun startPlayer() {
        player?.start()
        _state.update { MusicPlayerState.Playing(getCurrentPlayerPosition()) }
        startTimer()
    }

    override fun pausePlayer() {
        player?.pause()
        _state.update { MusicPlayerState.Paused(getCurrentPlayerPosition()) }
        stopTimer()
    }

    override fun getPlayerState(): StateFlow<MusicPlayerState> = state

    private fun initPlayer() {
        if (track?.previewUrl?.isEmpty() ?: true) return

        player = MediaPlayer()
        player?.setDataSource(track?.previewUrl)
        player?.prepareAsync()
        player?.setOnPreparedListener {
            _state.update { MusicPlayerState.Prepared }
        }
        player?.setOnCompletionListener {
            stopTimer()
            _state.update { MusicPlayerState.Prepared }
            if (isForegroundRunning) {
                stopForeground()
            }
        }
    }

    private fun startTimer() {
        updateTimerJob?.cancel()
        updateTimerJob = CoroutineScope(Dispatchers.IO).launch {
            while (player?.isPlaying ?: false) {
                delay(TRACK_TIME_UPDATE_INTERVAL)
                _state.update { MusicPlayerState.Playing(getCurrentPlayerPosition()) }
            }
        }
    }

    override fun startForeground() {
        if (_state.value !is MusicPlayerState.Playing) return

        isForegroundRunning = true
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK,
        )
    }

    override fun stopForeground() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        isForegroundRunning = false
    }

    private fun stopTimer() {
        updateTimerJob?.cancel()
    }

    private fun releasePlayer() {
        player?.stop()
        player?.setOnPreparedListener(null)
        player?.setOnCompletionListener(null)
        player?.release()
        player = null
    }

    inner class MusicServiceBinder : Binder() {
        fun getService() = this@MusicService
    }

    private fun formatTime(time: Int): String = Track.trackTimeFormat.format(time)
    private fun getCurrentPlayerPosition() = formatTime(player?.currentPosition ?: 0)

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "PlaylistMaker music service",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "PlaylistMaker: service for playing music"
        }

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(
                track?.let {
                    getString(
                        R.string.track_notification,
                        track?.artistName,
                        track?.trackName
                    )
                } ?: ""
            )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }

    companion object {
        private val TRACK_TIME_UPDATE_INTERVAL = 300.milliseconds
        private const val PLAYER_TRACK_KEY = "track"
        private const val NOTIFICATION_CHANNEL_ID = "playlist_maker_music_service"
        private const val SERVICE_NOTIFICATION_ID = 420

        fun createIntent(context: Context, track: Track?) =
            Intent(context, MusicService::class.java).apply {
                putExtra(PLAYER_TRACK_KEY, track)
            }
    }
}
