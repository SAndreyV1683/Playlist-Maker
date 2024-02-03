package a.sboev.ru.playlistmaker.creators

import a.sboev.ru.playlistmaker.search.data.TrackRepositoryImpl
import a.sboev.ru.playlistmaker.search.data.network.RetrofitNetworkClient
import a.sboev.ru.playlistmaker.search.domain.api.TrackInteractor
import a.sboev.ru.playlistmaker.search.domain.api.TrackRepository
import a.sboev.ru.playlistmaker.search.domain.impl.TrackInteractorImpl

object TrackInteractorCreator {
    private fun getTrackRepository(): TrackRepository = TrackRepositoryImpl(RetrofitNetworkClient())
    fun provideTrackInteractor(): TrackInteractor = TrackInteractorImpl(getTrackRepository())
}