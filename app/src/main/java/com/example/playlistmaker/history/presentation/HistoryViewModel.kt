package com.example.playlistmaker.history.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
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
        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = Creator.provideTracksHistoryInteractor(context)
                HistoryViewModel(interactor)
            }
        }

        data class HistoryState(val tracks: List<Track>, val isVisible: Boolean)
    }
}