package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.ITunesApiService
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TRACK_SERVICE_BASE_URL = "https://itunes.apple.com/"

val searchModule = module {
    single<ITunesApiService> {
        Retrofit
            .Builder()
            .baseUrl(TRACK_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    viewModel {
        SearchViewModel(get())
    }
}