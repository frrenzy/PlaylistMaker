package com.example.playlistmaker.history

interface History<T> {
    val size: Int
    fun add(item: T)
    fun getAll(): ArrayList<T>
    fun clear()
}