package com.example.playlistmaker.library.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouritesViewModel(private val favouritesInteractor: FavouriteTracksInteractor) :
    ViewModel() {
    private val _favourites = MutableStateFlow<FavouritesState>(FavouritesState.Empty)
    val favourites = _favourites.asStateFlow()

    fun loadTracks() {
        viewModelScope.launch {
            favouritesInteractor.getTracks().collect {
                val state = if (it.isEmpty()) FavouritesState.Empty
                else FavouritesState.Tracks(it)

                _favourites.value = state
            }
        }
    }
}

sealed interface FavouritesState {
    data class Tracks(val tracks: List<Track>) : FavouritesState
    object Empty : FavouritesState
}
