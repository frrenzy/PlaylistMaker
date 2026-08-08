package com.example.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.SearchResult
import com.example.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(private val searchInteractor: TracksInteractor) : ViewModel() {
    private val searchStateLiveData = MutableLiveData<SearchState>(SearchState.Default)
    fun observeSearchState(): LiveData<SearchState> = searchStateLiveData

    private var searchTerm = ""

    private var searchJob: Job? = null

    fun onPressEnter() {
        loadTracks(0.milliseconds)
    }

    fun onSearchTextChanged(s: CharSequence?) {
        searchTerm = s?.toString().orEmpty()
        loadTracks()
    }

    fun onReload() {
        loadTracks(0.milliseconds)
    }

    fun onClear() {
        sendUpdate(SearchState.Default)
    }

    private fun loadTracks(searchDelay: Duration = SEARCH_DEBOUNCE_DELAY) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(searchDelay)
            doSearchRequest()
        }
    }


    private suspend fun doSearchRequest() {
        if (searchTerm.isEmpty()) {
            return
        }

        sendUpdate(SearchState.Loading)

        searchInteractor.searchTracks(searchTerm).collect {
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
        searchJob?.cancel()
    }

    companion object {
        val SEARCH_DEBOUNCE_DELAY = 2000.milliseconds
    }
}

sealed interface SearchState {
    object Default : SearchState
    object Error : SearchState
    object Empty : SearchState
    object Loading : SearchState
    data class Content(val tracks: List<Track>) : SearchState
}
