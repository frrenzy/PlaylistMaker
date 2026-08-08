package com.example.playlistmaker.common.di

import com.example.playlistmaker.history.data.HistoryClient
import com.example.playlistmaker.history.data.SharedPrefsHistoryClient
import com.example.playlistmaker.history.data.TracksHistoryRepositoryImpl
import com.example.playlistmaker.history.domain.TracksHistoryInteractor
import com.example.playlistmaker.history.domain.TracksHistoryRepository
import com.example.playlistmaker.history.domain.impl.TracksHistoryInteractorImpl
import com.example.playlistmaker.history.presentation.HistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val historyModule = module {
    single<HistoryClient> { (maxDepth: Int) ->
        SharedPrefsHistoryClient(get(), get(), maxDepth)
    }

    single<TracksHistoryRepository> { (maxDepth: Int) ->
        TracksHistoryRepositoryImpl(get {
            parametersOf(maxDepth)
        })
    }

    single<TracksHistoryInteractor> { (maxDepth: Int) ->
        TracksHistoryInteractorImpl(get {
            parametersOf(maxDepth)
        })
    }

    viewModel {
        HistoryViewModel(get {
            parametersOf(10)
        })
    }
}
