package a.sboev.ru.playlistmaker.settings.data.local

import a.sboev.ru.playlistmaker.App
import a.sboev.ru.playlistmaker.settings.data.SettingsHandler
import android.content.SharedPreferences

class ThemeSettingsHandler(private val sharedPreferences: SharedPreferences): SettingsHandler {
    override fun getThemeSettings(): Boolean {
        return sharedPreferences.getBoolean("isDarkTheme", false)
    }

    override fun updateThemeSetting(isDark: Boolean) {
        sharedPreferences.edit().putBoolean("isDarkTheme", isDark).apply()
    }
}