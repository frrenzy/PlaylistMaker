package com.example.playlistmaker.library.domain.models

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val coverPath: String?,
    val amount: Int = 0
)
