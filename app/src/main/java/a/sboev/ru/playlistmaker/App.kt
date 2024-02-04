package a.sboev.ru.playlistmaker

import a.sboev.ru.playlistmaker.creators.SettingsInteractorCreator
import a.sboev.ru.playlistmaker.settings.domain.ThemeSettings
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsInteractor
import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM


class App: Application() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var settingsInteractor: SettingsInteractor
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        sharedPreferences = getSharedPreferences(PLAY_LIST_MAKER_SHARED_PREFS, MODE_PRIVATE)
        settingsInteractor = SettingsInteractorCreator.provideSettingsInteractor()
        val isDark = settingsInteractor.getThemeSettings().darkTheme
        if (!isDark) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            settingsInteractor.updateThemeSetting(ThemeSettings(true))
        }
    }

    companion object {
        const val PLAY_LIST_MAKER_SHARED_PREFS = "play_list_maker_shared_prefs "
        lateinit var INSTANCE: App
    }
}

