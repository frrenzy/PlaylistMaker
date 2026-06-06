package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track

interface TracksRepository {
    fun searchTracks(term: String): List<Track>
}