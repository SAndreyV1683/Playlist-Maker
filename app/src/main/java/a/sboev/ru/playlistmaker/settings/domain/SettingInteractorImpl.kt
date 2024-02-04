package a.sboev.ru.playlistmaker.settings.domain

import a.sboev.ru.playlistmaker.settings.domain.api.SettingsInteractor
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsRepository

class SettingInteractorImpl(private val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}