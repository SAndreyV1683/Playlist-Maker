package a.sboev.ru.playlistmaker.settings.data

interface SettingsHandler {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(isDark: Boolean)
}