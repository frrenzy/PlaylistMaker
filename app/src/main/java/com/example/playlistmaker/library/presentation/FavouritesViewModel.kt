package com.example.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import kotlinx.coroutines.launch

class FavouritesViewModel(private val favouritesInteractor: FavouriteTracksInteractor) :
    ViewModel() {
    private var favouritesLiveData = MutableLiveData<FavouritesState>()
    fun observeFavouritesState(): LiveData<FavouritesState> = favouritesLiveData

    init {
        loadTracks()
    }

    fun loadTracks() {
        viewModelScope.launch {
            favouritesInteractor.getTracks().collect {
                val state = if (it.isEmpty()) FavouritesState.Empty
                else FavouritesState.Tracks(it)

                favouritesLiveData.postValue(state)
            }
        }
    }
}

sealed interface FavouritesState {
    data class Tracks(val tracks: List<Track>) : FavouritesState
    object Empty : FavouritesState
}
