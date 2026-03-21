package com.example.playlistmaker.track.history

import android.content.SharedPreferences
import com.example.playlistmaker.history.History
import com.example.playlistmaker.track.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val TRACKS_SEARCH_HISTORY_KEY = "tracks_search_history"

class TrackSearchHistory(val preferences: SharedPreferences, val maxDepth: Int = 10) :
    History<Track> {
    override var size: Int = 0
        private set

    override fun add(item: Track) {
        val savedTracks = loadFromPreferences()
        var newTracks = mutableListOf(item)

        savedTracks.filterTo(newTracks) { it.trackId != item.trackId }
        if (newTracks.size > maxDepth) {
            newTracks = ArrayList(newTracks.slice(0..<maxDepth))
        }
        size = newTracks.size

        saveToPreferences(newTracks)
    }

    override fun getAll(): ArrayList<Track> {
        val tracks = loadFromPreferences()
        size = tracks.size

        return tracks
    }

    override fun clear() {
        saveToPreferences(emptyList())
        size = 0
    }

    private fun saveToPreferences(tracks: List<Track>) {
        val serializedTracks = Gson().toJson(tracks)
        preferences
            .edit()
            .putString(TRACKS_SEARCH_HISTORY_KEY, serializedTracks)
            .apply()
    }

    private fun loadFromPreferences(): ArrayList<Track> {
        val serializedTracks =
            preferences.getString(TRACKS_SEARCH_HISTORY_KEY, null) ?: return arrayListOf()
        return Gson().fromJson(serializedTracks, object : TypeToken<ArrayList<Track>>() {}.type)
    }
}