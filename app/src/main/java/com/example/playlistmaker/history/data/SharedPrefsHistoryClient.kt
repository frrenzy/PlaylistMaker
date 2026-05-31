package com.example.playlistmaker.history.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.search.data.dto.TrackDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefsHistoryClient(val preferences: SharedPreferences, val maxDepth: Int = 10) :
    HistoryClient {
    private val gson = Gson()

    override var size: Int = 0
        private set

    override fun add(item: TrackDto) {
        val savedTracks = loadFromPreferences()
        var newTracks = mutableListOf(item)

        savedTracks.filterTo(newTracks) { it.trackId != item.trackId }
        if (newTracks.size > maxDepth) {
            newTracks = ArrayList(newTracks.slice(0 until maxDepth))
        }
        size = newTracks.size

        saveToPreferences(newTracks)
    }

    override fun getAll(): List<TrackDto> {
        val tracks = loadFromPreferences()
        size = tracks.size

        return tracks
    }

    override fun clear() {
        saveToPreferences(emptyList())
        size = 0
    }

    private fun saveToPreferences(tracks: List<TrackDto>) {
        val serializedTracks = gson.toJson(tracks)
        preferences.edit {
            putString(
                TRACKS_SEARCH_HISTORY_KEY,
                serializedTracks
            )
        }
    }

    private fun loadFromPreferences(): List<TrackDto> {
        val serializedTracks =
            preferences.getString(TRACKS_SEARCH_HISTORY_KEY, null) ?: return emptyList()
        return gson.fromJson(serializedTracks, object : TypeToken<List<TrackDto>>() {}.type)
    }

    companion object {
        private const val TRACKS_SEARCH_HISTORY_KEY = "tracks_search_history"
    }
}