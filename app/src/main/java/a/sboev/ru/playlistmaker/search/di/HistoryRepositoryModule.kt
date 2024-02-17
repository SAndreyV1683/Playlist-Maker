package a.sboev.ru.playlistmaker.search.di

import a.sboev.ru.playlistmaker.search.data.History
import a.sboev.ru.playlistmaker.search.data.HistoryRepositoryImpl
import a.sboev.ru.playlistmaker.search.data.local.SearchHistoryHandler
import a.sboev.ru.playlistmaker.search.domain.api.HistoryInteractor
import a.sboev.ru.playlistmaker.search.domain.api.HistoryRepository
import a.sboev.ru.playlistmaker.search.domain.impl.HistoryInteractorImpl
import android.content.Context.MODE_PRIVATE
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

const val PLAY_LIST_MAKER_SHARED_PREFS = "play_list_maker_shared_prefs"

val historyRepositoryModule = module {

    single {
        androidContext().getSharedPreferences(PLAY_LIST_MAKER_SHARED_PREFS, MODE_PRIVATE)
    }

    factory<History> {
        SearchHistoryHandler(get())
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(get())
    }

    factory <HistoryInteractor> {
        HistoryInteractorImpl(get())
    }

}