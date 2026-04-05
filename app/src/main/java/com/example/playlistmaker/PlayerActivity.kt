package com.example.playlistmaker

import android.os.Bundle
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
import com.example.playlistmaker.track.model.Track
import com.example.playlistmaker.utils.connectBackButton

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

        track = intent.getParcelableExtra(PLAYER_TRACK_KEY, Track::class.java)
            ?: return

        drawTrack()
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
            .placeholder(R.drawable.ic_placeholder_312)
            .centerCrop()
            .transform(RoundedCorners(TRACK_ART_CORNER_RADIUS))
            .into(trackCover)
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

    companion object {
        const val TRACK_ART_CORNER_RADIUS = 8
        const val TRACK_BUNDLE_KEY = "track"
    }
}
