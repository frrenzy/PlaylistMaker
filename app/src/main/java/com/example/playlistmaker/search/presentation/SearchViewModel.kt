package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.SearchResult
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track

class SearchViewModel(private val searchInteractor: TracksInteractor) : ViewModel() {
    private val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Default)
    fun observeSearchState(): LiveData<SearchState> = searchStateLiveData

    private var searchTerm = ""
    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())

    fun onPressEnter() {
        loadTracks(0L)
    }

    fun onSearchTextChanged(s: CharSequence?) {
        searchTerm = s?.toString().orEmpty()
        loadTracks()
    }

    fun onReload() {
        loadTracks(0L)
    }

    fun onClear() {
        sendUpdate(SearchState.Default)
    }

    private fun loadTracks(delay: Long = SEARCH_DEBOUNCE_DELAY) {
        handler.removeCallbacks(searchRunnable)
        if (delay == 0L) handler.post(searchRunnable) else handler.postDelayed(
            searchRunnable,
            delay
        )
    }


    private fun searchRequest() {
        if (searchTerm.isEmpty()) {
            return
        }

        sendUpdate(SearchState.Loading)

        searchInteractor.searchTracks(searchTerm) {
            val state = when (it) {
                is SearchResult.Success -> SearchState.Content(it.tracks)
                is SearchResult.Empty -> SearchState.Empty
                is SearchResult.Error -> SearchState.Error
            }
            sendUpdate(state)
        }
    }

    fun sendUpdate(state: SearchState) {
        searchStateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}

sealed interface SearchState {
    object Default : SearchState
    object Error : SearchState
    object Empty : SearchState
    object Loading : SearchState
    data class Content(val tracks: List<Track>) : SearchState
}
