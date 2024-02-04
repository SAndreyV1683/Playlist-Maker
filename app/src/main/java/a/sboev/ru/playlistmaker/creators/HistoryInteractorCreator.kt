package a.sboev.ru.playlistmaker.creators

import a.sboev.ru.playlistmaker.search.data.HistoryRepositoryImpl
import a.sboev.ru.playlistmaker.search.data.local.SearchHistoryHandler
import a.sboev.ru.playlistmaker.search.domain.api.HistoryInteractor
import a.sboev.ru.playlistmaker.search.domain.api.HistoryRepository
import a.sboev.ru.playlistmaker.search.domain.impl.HistoryInteractorImpl

object HistoryInteractorCreator {
    private fun getSearchHistoryHandler(): HistoryRepository = HistoryRepositoryImpl(SearchHistoryHandler())
    fun provideHistoryInteractor(): HistoryInteractor = HistoryInteractorImpl(getSearchHistoryHandler())
}