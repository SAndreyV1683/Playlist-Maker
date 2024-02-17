package a.sboev.ru.playlistmaker.search.di

import a.sboev.ru.playlistmaker.search.data.NetworkClient
import a.sboev.ru.playlistmaker.search.data.TrackRepositoryImpl
import a.sboev.ru.playlistmaker.search.data.network.RetrofitNetworkClient
import a.sboev.ru.playlistmaker.search.domain.api.TrackInteractor
import a.sboev.ru.playlistmaker.search.domain.api.TrackRepository
import a.sboev.ru.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val trackRepositoryModule = module {

    factory<NetworkClient> {
        RetrofitNetworkClient()
    }

    factory<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }

}