package com.example.playlistmaker.track.model

import android.icu.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String?
) {
    val trackTime: String
        get() = trackTimeFormat.format(trackTimeMillis)

    companion object {
        val trackTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    }

}