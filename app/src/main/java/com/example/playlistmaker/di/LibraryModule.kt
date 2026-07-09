package com.example.playlistmaker.di

import com.example.playlistmaker.library.presentation.FavouritesViewModel
import com.example.playlistmaker.library.presentation.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    viewModel {
        FavouritesViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }
}
