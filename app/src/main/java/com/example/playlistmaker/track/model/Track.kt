package com.example.playlistmaker.track.model

import android.icu.text.SimpleDateFormat
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
) : Parcelable {
    val trackTime: String
        get() = trackTimeFormat.format(trackTimeMillis)
    val coverArtworkUrl: String?
        get() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    val year: String?
        get() = releaseDate?.substringBefore('-')

    companion object {
        val trackTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    }

}