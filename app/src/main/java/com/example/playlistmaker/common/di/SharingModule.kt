package com.example.playlistmaker.common.di

import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingModule = module {
    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }
}
