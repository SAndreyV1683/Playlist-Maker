package a.sboev.ru.playlistmaker.settings.domain.api

import a.sboev.ru.playlistmaker.settings.domain.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}