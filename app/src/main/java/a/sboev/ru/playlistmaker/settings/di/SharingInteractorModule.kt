package a.sboev.ru.playlistmaker.settings.di

import a.sboev.ru.playlistmaker.settings.data.sharing.ExternalNavigator
import a.sboev.ru.playlistmaker.settings.domain.SharingInteractorImpl
import a.sboev.ru.playlistmaker.settings.domain.api.SharingInteractor
import org.koin.dsl.module

val sharingInteractorModule = module {

    factory {
        ExternalNavigator()
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

}