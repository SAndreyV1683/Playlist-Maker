package a.sboev.ru.playlistmaker.settings.domain.api

import a.sboev.ru.playlistmaker.settings.domain.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}