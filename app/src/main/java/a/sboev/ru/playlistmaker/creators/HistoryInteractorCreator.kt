package a.sboev.ru.playlistmaker.creators

import a.sboev.ru.playlistmaker.search.data.HistoryRepositoryImpl
import a.sboev.ru.playlistmaker.search.data.local.SearchHistoryHandler
import a.sboev.ru.playlistmaker.search.domain.api.HistoryInteractor
import a.sboev.ru.playlistmaker.search.domain.api.HistoryRepository
import a.sboev.ru.playlistmaker.search.domain.impl.HistoryInteractorImpl
import android.content.SharedPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object HistoryInteractorCreator: KoinComponent {

    private val sharedPreferences: SharedPreferences by inject()
    private fun getSearchHistoryHandler(): HistoryRepository = HistoryRepositoryImpl(SearchHistoryHandler(sharedPreferences))
    fun provideHistoryInteractor(): HistoryInteractor = HistoryInteractorImpl(getSearchHistoryHandler())

}