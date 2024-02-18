package a.sboev.ru.playlistmaker.search.di

import a.sboev.ru.playlistmaker.search.data.NetworkClient
import a.sboev.ru.playlistmaker.search.data.TrackRepositoryImpl
import a.sboev.ru.playlistmaker.search.data.network.ITunesApiService
import a.sboev.ru.playlistmaker.search.data.network.RetrofitNetworkClient
import a.sboev.ru.playlistmaker.search.domain.api.TrackInteractor
import a.sboev.ru.playlistmaker.search.domain.api.TrackRepository
import a.sboev.ru.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseUrl = "https://itunes.apple.com"

val trackRepositoryModule = module {

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    factory<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }

}