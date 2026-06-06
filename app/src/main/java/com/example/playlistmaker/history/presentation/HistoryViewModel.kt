package com.example.playlistmaker.history.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.history.domain.TracksHistoryInteractor
import com.example.playlistmaker.search.domain.models.Track

class HistoryViewModel(private val historyInteractor: TracksHistoryInteractor) : ViewModel() {
    private val historyLiveData = MutableLiveData(
        HistoryState(
            historyInteractor.getSavedTracks(),
            false,
        )
    )

    fun observeHistory(): LiveData<HistoryState> = historyLiveData

    private var isSearchFocused = false
    private var isSearchEmpty = true

    fun onClear() {
        historyInteractor.clearHistory()
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

    fun onSearchTextChanged(s: CharSequence?) {
        isSearchEmpty = s.isNullOrEmpty()
        sendUpdate()
    }

    private fun sendUpdate() {
        val tracks = historyInteractor.getSavedTracks()
        historyLiveData.postValue(
            HistoryState(
                tracks,
                !tracks.isEmpty() && isSearchEmpty && isSearchFocused,
            )
        )
    }

    companion object {
        data class HistoryState(val tracks: List<Track>, val isVisible: Boolean)
    }
}