package a.sboev.ru.playlistmaker.settings.di

import a.sboev.ru.playlistmaker.settings.data.SettingsHandler
import a.sboev.ru.playlistmaker.settings.data.SettingsRepositoryImpl
import a.sboev.ru.playlistmaker.settings.data.local.ThemeSettingsHandler
import a.sboev.ru.playlistmaker.settings.domain.SettingInteractorImpl
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsInteractor
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsRepository
import org.koin.dsl.module

val settingsRepositoryModule = module {

    single<SettingsHandler> {
        ThemeSettingsHandler(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<SettingsInteractor> {
        SettingInteractorImpl(get())
    }
}