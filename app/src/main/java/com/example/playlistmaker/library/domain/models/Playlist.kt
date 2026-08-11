package com.example.playlistmaker.library.domain.models

import com.example.playlistmaker.common.domain.models.Track

data class Playlist(val name: String, val tracks: List<Track>)
