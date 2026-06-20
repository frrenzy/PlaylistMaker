package com.example.playlistmaker.library.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track

class FavouritesViewModel : ViewModel() {
    private var favouritesLiveData = MutableLiveData<FavouritesState>(FavouritesState.Empty)
    fun observeFavouritesState(): LiveData<FavouritesState> = favouritesLiveData
}

sealed interface FavouritesState {
    data class Tracks(val tracks: List<Track>) : FavouritesState
    object Empty : FavouritesState
}
