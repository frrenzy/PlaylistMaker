package com.example.playlistmaker.sharing.domain.model

data class EmailData(
    val addresses: Array<String>,
    val subject: String,
    val message: String
)