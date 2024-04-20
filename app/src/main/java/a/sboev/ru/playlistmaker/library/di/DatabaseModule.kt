package a.sboev.ru.playlistmaker.library.di

import a.sboev.ru.playlistmaker.library.data.converter.TrackDbConverter
import a.sboev.ru.playlistmaker.library.data.db.AppDatabase
import a.sboev.ru.playlistmaker.library.data.impl.DatabaseRepositoryImpl
import a.sboev.ru.playlistmaker.library.domain.api.DatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.api.DatabaseRepository
import a.sboev.ru.playlistmaker.library.domain.impl.DatabaseInteractorImpl
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {

    single<DatabaseRepository> {
        DatabaseRepositoryImpl(get(), get())
    }

    single<DatabaseInteractor> {
        DatabaseInteractorImpl(get())
    }

    factory { TrackDbConverter() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

}