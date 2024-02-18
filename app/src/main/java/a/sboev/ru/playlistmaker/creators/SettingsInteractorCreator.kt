package a.sboev.ru.playlistmaker.creators

import a.sboev.ru.playlistmaker.settings.data.SettingsRepositoryImpl
import a.sboev.ru.playlistmaker.settings.data.local.ThemeSettingsHandler
import a.sboev.ru.playlistmaker.settings.domain.SettingInteractorImpl
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsInteractor
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsRepository
import android.content.SharedPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SettingsInteractorCreator: KoinComponent {

    private val sharedPreferences: SharedPreferences by inject()
    private fun getSettingsRepository(): SettingsRepository = SettingsRepositoryImpl(ThemeSettingsHandler(sharedPreferences))
    fun provideSettingsInteractor(): SettingsInteractor = SettingInteractorImpl(getSettingsRepository())

}