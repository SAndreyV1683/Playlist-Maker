package a.sboev.ru.playlistmaker.creators

import a.sboev.ru.playlistmaker.settings.data.SettingsRepositoryImpl
import a.sboev.ru.playlistmaker.settings.data.local.ThemeSettingsHandler
import a.sboev.ru.playlistmaker.settings.domain.SettingInteractorImpl
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsInteractor
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsRepository

object SettingsInteractorCreator {
    private fun getSettingsRepository(): SettingsRepository = SettingsRepositoryImpl(ThemeSettingsHandler())
    fun provideSettingsInteractor(): SettingsInteractor = SettingInteractorImpl(getSettingsRepository())
}