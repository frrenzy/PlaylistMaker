package com.example.playlistmaker.common.di

import com.example.playlistmaker.common.domain.models.Track
import com.example.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    viewModel { (track: Track) ->
        PlayerViewModel(track, get(), get())
    }
}
