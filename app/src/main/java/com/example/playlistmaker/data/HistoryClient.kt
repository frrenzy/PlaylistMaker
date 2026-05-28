package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackDto

interface HistoryClient {
    val size: Int
    fun add(item: TrackDto)
    fun getAll(): List<TrackDto>
    fun clear()
}