package com.example.playlistmaker.history.data

import com.example.playlistmaker.search.data.dto.TrackDto

interface HistoryClient {
    val size: Int
    fun add(item: TrackDto)
    fun getAll(): List<TrackDto>
    fun clear()
}