package a.sboev.ru.playlistmaker.settings.data

import a.sboev.ru.playlistmaker.settings.domain.api.SettingsRepository
import a.sboev.ru.playlistmaker.settings.domain.ThemeSettings
import androidx.appcompat.app.AppCompatDelegate

class SettingsRepositoryImpl(private val settingsHandler: SettingsHandler): SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        val isDarkTheme = settingsHandler.getThemeSettings()
        return ThemeSettings(isDarkTheme)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsHandler.updateThemeSetting(isDark = settings.darkTheme)
        switchTheme(settings.darkTheme)
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}