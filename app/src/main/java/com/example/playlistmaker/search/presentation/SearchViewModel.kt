package com.example.playlistmaker.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.search.domain.SearchResult
import com.example.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class SearchViewModel(private val searchInteractor: TracksInteractor) : ViewModel() {
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Default)
    val searchState = _searchState.asStateFlow()

    private val _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    private var searchJob: Job? = null

    fun onPressEnter() {
        loadTracks(0.milliseconds)
    }

    fun onSearchTextChanged(s: String) {
        _searchTerm.value = s
        loadTracks()
    }

    fun onReload() {
        loadTracks(0.milliseconds)
    }

    fun onClear() {
        sendUpdate(SearchState.Default)
        _searchTerm.value = ""
    }

    private fun loadTracks(searchDelay: Duration = SEARCH_DEBOUNCE_DELAY) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(searchDelay)
            doSearchRequest()
        }
    }


    private suspend fun doSearchRequest() {
        if (_searchTerm.value.isEmpty()) {
            return
        }

        sendUpdate(SearchState.Loading)

        searchInteractor.searchTracks(_searchTerm.value).collect {
            val state = when (it) {
                is SearchResult.Success -> SearchState.Content(it.tracks)
                is SearchResult.Empty -> SearchState.Empty
                is SearchResult.Error -> SearchState.Error
            }
            sendUpdate(state)
        }

    }

    fun sendUpdate(state: SearchState) {
        _searchState.value = state
    }

    override fun onCleared() {
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
