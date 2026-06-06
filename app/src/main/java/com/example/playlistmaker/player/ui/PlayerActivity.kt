package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.PLAYER_TRACK_KEY
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.presentation.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.connectBackButton
import com.example.playlistmaker.utils.dp

class PlayerActivity : AppCompatActivity() {
    private lateinit var viewModel: PlayerViewModel

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        connectBackButton(binding.backButton)

        val track = intent.getParcelableExtra(PLAYER_TRACK_KEY, Track::class.java)
            ?: return

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getFactory(track)
        ).get(PlayerViewModel::class.java)

        viewModel.observeTrack().observe(this) {
            drawTrack(it)
        }
        viewModel.observePlayerState().observe(this) {
            binding.apply {
                playButton.isEnabled = it.isPlayButtonEnabled
                playTime.text = it.progressTime
                playButton.background = when (it.state) {
                    PlayerViewModel.MediaState.PLAYING -> getDrawable(R.drawable.pause_button)
                    else -> getDrawable(R.drawable.play_button)
                }
            }
        }
        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClick()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPaused()
    }

    private fun drawTrack(track: Track) {
        binding.apply {
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
    }

    companion object {
        const val TRACK_ART_CORNER_RADIUS = 8
    }
}