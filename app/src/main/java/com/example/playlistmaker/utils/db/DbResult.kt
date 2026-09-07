package com.example.playlistmaker.utils.db

sealed interface DbResult {
    object Conflict : DbResult
    data class Success(val id: Long) : DbResult
}
