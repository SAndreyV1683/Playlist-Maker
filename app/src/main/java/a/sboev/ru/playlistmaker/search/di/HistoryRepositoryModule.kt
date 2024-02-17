package a.sboev.ru.playlistmaker.search.di

import a.sboev.ru.playlistmaker.search.data.History
import a.sboev.ru.playlistmaker.search.data.HistoryRepositoryImpl
import a.sboev.ru.playlistmaker.search.data.local.SearchHistoryHandler
import a.sboev.ru.playlistmaker.search.domain.api.HistoryInteractor
import a.sboev.ru.playlistmaker.search.domain.api.HistoryRepository
import a.sboev.ru.playlistmaker.search.domain.impl.HistoryInteractorImpl
import org.koin.dsl.module

val historyRepositoryModule = module {

    factory<History> {
        SearchHistoryHandler()
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(get())
    }

    factory <HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

}