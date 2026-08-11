package com.example.playlistmaker.common.di

import com.example.playlistmaker.library.data.FavouritesRepositoryImpl
import com.example.playlistmaker.library.data.converters.TrackDbConverter
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import com.example.playlistmaker.library.domain.FavouritesRepository
import com.example.playlistmaker.library.domain.impl.FavouriteTracksInteractorImpl
import com.example.playlistmaker.library.presentation.FavouritesViewModel
import com.example.playlistmaker.library.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    single<TrackDbConverter> {
        TrackDbConverter()
    }

    single<FavouritesRepository> {
        FavouritesRepositoryImpl(get(), get())
    }

    single<FavouriteTracksInteractor> {
        FavouriteTracksInteractorImpl(get())
    }

    viewModel {
        FavouritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel()
    }
}
