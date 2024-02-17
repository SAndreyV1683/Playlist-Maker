package a.sboev.ru.playlistmaker

import a.sboev.ru.playlistmaker.audioplayer.di.audioPlayerModule
import a.sboev.ru.playlistmaker.di.viewModelModule
import a.sboev.ru.playlistmaker.creators.SettingsInteractorCreator
import a.sboev.ru.playlistmaker.search.di.historyRepositoryModule
import a.sboev.ru.playlistmaker.search.di.trackRepositoryModule
import a.sboev.ru.playlistmaker.settings.di.settingsRepositoryModule
import a.sboev.ru.playlistmaker.settings.di.sharingInteractorModule
import a.sboev.ru.playlistmaker.settings.domain.ThemeSettings
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsInteractor
import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent.inject


class App: Application() {

    private val settingsInteractor: SettingsInteractor by inject(SettingsInteractor::class.java)
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        startKoin {
            androidContext(this@App)
            modules(
                audioPlayerModule,
                viewModelModule,
                historyRepositoryModule,
                trackRepositoryModule,
                settingsRepositoryModule,
                sharingInteractorModule
            )
        }
        val isDark = settingsInteractor.getThemeSettings().darkTheme
        if (!isDark) {
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            settingsInteractor.updateThemeSetting(ThemeSettings(true))
        }
    }

    companion object {
        lateinit var INSTANCE: App
    }
}

