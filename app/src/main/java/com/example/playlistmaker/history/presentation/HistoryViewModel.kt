package com.example.playlistmaker.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.history.domain.TracksHistoryInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyInteractor: TracksHistoryInteractor) : ViewModel() {
    private val _history = MutableStateFlow<HistoryState>(HistoryState(emptyList(), false))
    val history = _history.asStateFlow()

    private var isSearchFocused = false
    private var isSearchEmpty = true

    fun loadSavedTracks() {
        viewModelScope.launch {
            _history.value = HistoryState(
                historyInteractor.getSavedTracks(),
                false,
            )
        }
    }

    fun onClearHistory() {
        historyInteractor.clearHistory()
        sendUpdate()
    }

    fun onClearSearch() {
        isSearchEmpty = true
        sendUpdate()
    }

    fun onTrackClick(track: Track) {
        historyInteractor.saveTrack(track)
        sendUpdate()
    }

    fun onSearchFocus(hasFocus: Boolean) {
        isSearchFocused = hasFocus
        sendUpdate()
    }

    fun onSearchTextChanged(s: String) {
        isSearchEmpty = s.isEmpty()
        sendUpdate()
    }

    private fun sendUpdate() {
        viewModelScope.launch {
            val tracks = historyInteractor.getSavedTracks()
            _history.value = HistoryState(
                tracks,
                !tracks.isEmpty() && isSearchEmpty && isSearchFocused,
            )
        }
    }

    companion object {
        data class HistoryState(val tracks: List<Track>, val isVisible: Boolean)
    }
}
