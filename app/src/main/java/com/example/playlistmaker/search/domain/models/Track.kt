package com.example.playlistmaker.search.domain.models

import android.icu.text.SimpleDateFormat
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val coverArtworkUrl: String?,
    val year: String?,
) : Parcelable {
    val trackTime: String
        get() = trackTimeFormat.format(trackTimeMillis)

    companion object {
        val trackTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        val DEFAULT_TRACK_TIME = trackTimeFormat.format(0)
    }
}
