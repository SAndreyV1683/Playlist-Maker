package a.sboev.ru.playlistmaker.library.di

import a.sboev.ru.playlistmaker.library.data.FilesHandler
import a.sboev.ru.playlistmaker.library.data.impl.FilesRepositoryImpl
import a.sboev.ru.playlistmaker.library.data.local.ImageFilesHandler
import a.sboev.ru.playlistmaker.library.domain.api.FilesInteractor
import a.sboev.ru.playlistmaker.library.domain.api.FilesRepository
import a.sboev.ru.playlistmaker.library.domain.impl.FilesInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val filesModule = module {

    single<FilesHandler> {
        ImageFilesHandler(androidContext())
    }

    single<FilesRepository> {
        FilesRepositoryImpl(get())
    }

    single<FilesInteractor> {
        FilesInteractorImpl(get())
    }
}