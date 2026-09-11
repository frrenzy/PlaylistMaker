package com.example.playlistmaker.common.di

import com.example.playlistmaker.library.data.FavouritesRepositoryImpl
import com.example.playlistmaker.library.data.PlaylistsRepositoryImpl
import com.example.playlistmaker.library.data.converters.PlaylistDbConverter
import com.example.playlistmaker.library.data.converters.TrackDbConverter
import com.example.playlistmaker.library.domain.FavouriteTracksInteractor
import com.example.playlistmaker.library.domain.FavouritesRepository
import com.example.playlistmaker.library.domain.PlaylistsInteractor
import com.example.playlistmaker.library.domain.PlaylistsRepository
import com.example.playlistmaker.library.domain.impl.FavouriteTracksInteractorImpl
import com.example.playlistmaker.library.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.library.presentation.CreatePlaylistViewModel
import com.example.playlistmaker.library.presentation.EditPlaylistViewModel
import com.example.playlistmaker.library.presentation.FavouritesViewModel
import com.example.playlistmaker.library.presentation.PlaylistViewModel
import com.example.playlistmaker.library.presentation.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
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

    single<PlaylistDbConverter> {
        PlaylistDbConverter()
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get(), androidContext())
    }

    single<PlaylistsInteractor> {
        PlaylistsInteractorImpl(get())
    }

    viewModel {
        FavouritesViewModel(get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        CreatePlaylistViewModel(get())
    }

    viewModel { (playlistId: Long) ->
        PlaylistViewModel(playlistId, get(), get())
    }

    viewModel { (playlistId: Long) ->
        EditPlaylistViewModel(playlistId, get())
    }
}
