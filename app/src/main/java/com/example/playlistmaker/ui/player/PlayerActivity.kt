package com.example.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.PLAYER_TRACK_KEY
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.connectBackButton
import com.example.playlistmaker.utils.dp

class PlayerActivity : AppCompatActivity() {
    private lateinit var trackName: TextView
    private lateinit var trackAuthor: TextView
    private lateinit var trackCover: ImageView
    private lateinit var trackTime: TextView
    private lateinit var trackAlbum: TextView
    private lateinit var trackAlbumGroup: Group
    private lateinit var trackYear: TextView
    private lateinit var trackYearGroup: Group
    private lateinit var trackGenre: TextView
    private lateinit var trackCountry: TextView
    private lateinit var track: Track
    private lateinit var playButton: ImageButton
    private lateinit var playTime: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val player = MediaPlayer()
    private var playerState = PlayerState.DEFAULT
    private var updateTrackTimeRunnable: Runnable = Runnable {
        player.currentPosition.let { Track.trackTimeFormat.format(it) }.also {
            playTime.text = it
        }
        handler.postDelayed(updateTrackTimeRunnable, TRACK_TIME_UPDATE_INTERVAL)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        connectBackButton(R.id.player_back_button)

        trackName = findViewById(R.id.track_name)
        trackAuthor = findViewById(R.id.track_author)
        trackCover = findViewById(R.id.track_cover)
        trackTime = findViewById(R.id.track_time)
        trackAlbum = findViewById(R.id.track_album)
        trackYear = findViewById(R.id.track_year)
        trackGenre = findViewById(R.id.track_genre)
        trackCountry = findViewById(R.id.track_country)
        trackAlbumGroup = findViewById(R.id.track_album_group)
        trackYearGroup = findViewById(R.id.track_year_group)

        playButton = findViewById(R.id.play_button)
        playTime = findViewById(R.id.play_time)

        playButton.setOnClickListener { playbackControl() }

        track = intent.getParcelableExtra(PLAYER_TRACK_KEY, Track::class.java)
            ?: return

        drawTrack()
        preparePlayer(track.previewUrl)
    }

    private fun drawTrack() {
        trackName.text = track.trackName
        trackAuthor.text = track.artistName
        trackTime.text = track.trackTime
        trackGenre.text = track.primaryGenreName
        trackCountry.text = track.country

        track.year?.let {
            trackYear.text = it
        } ?: run {
            trackYearGroup.isVisible = false
        }

        track.collectionName?.let {
            trackAlbum.text = it
        } ?: run {
            trackAlbumGroup.isVisible = false
        }

        Glide.with(this@PlayerActivity)
            .load(track.coverArtworkUrl)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ART_CORNER_RADIUS.dp))
            .into(trackCover)
    }

    private fun preparePlayer(url: String) {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = PlayerState.PREPARED
            playTime.text = Track.trackTimeFormat.format(0)
        }
        player.setOnCompletionListener {
            playerState = PlayerState.PREPARED
            playButton.background = getDrawable(R.drawable.play_button)
            handler.removeCallbacks(updateTrackTimeRunnable)
            playTime.text = Track.trackTimeFormat.format(0)
        }
    }

    private fun startPlayer() {
        player.start()
        playButton.background = getDrawable(R.drawable.pause_button)
        handler.post(updateTrackTimeRunnable)
        playerState = PlayerState.PLAYING
    }

    private fun pausePlayer() {
        player.pause()
        playButton.background = getDrawable(R.drawable.play_button)
        handler.removeCallbacks(updateTrackTimeRunnable)
        playerState = PlayerState.PAUSED
    }

    private fun playbackControl() =
        when (playerState) {
            PlayerState.PLAYING -> pausePlayer()
            PlayerState.PREPARED, PlayerState.PAUSED -> startPlayer()
            else -> Unit
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(TRACK_BUNDLE_KEY, track)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        track = savedInstanceState.getParcelable(TRACK_BUNDLE_KEY, Track::class.java) ?: return
        drawTrack()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        pausePlayer()
        player.release()
    }

    enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED,
    }

    companion object {
        const val TRACK_ART_CORNER_RADIUS = 8
        const val TRACK_BUNDLE_KEY = "track"
        const val TRACK_TIME_UPDATE_INTERVAL = 300L
    }
}