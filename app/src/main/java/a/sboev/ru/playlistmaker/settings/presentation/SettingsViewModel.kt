package a.sboev.ru.playlistmaker.settings.presentation

import a.sboev.ru.playlistmaker.settings.domain.ThemeSettings
import a.sboev.ru.playlistmaker.settings.domain.api.SettingsInteractor
import a.sboev.ru.playlistmaker.settings.domain.api.SharingInteractor
import a.sboev.ru.playlistmaker.settings.domain.model.EmailData
import androidx.lifecycle.ViewModel
import org.koin.java.KoinJavaComponent.inject

class SettingsViewModel: ViewModel() {

    private val settingsInteractor: SettingsInteractor by inject(SettingsInteractor::class.java)
    private val sharingInteractor: SharingInteractor by inject(SharingInteractor::class.java)

    fun getThemeSettings(): Boolean {
        return settingsInteractor.getThemeSettings().darkTheme
    }
    fun updateTheme(isDark: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isDark))
    }

    fun shareApp(link: String) {
        sharingInteractor.shareApp(link)
    }
    fun openTerms(link: String) {
        sharingInteractor.openTerms(link)
    }
    fun openSupport(emailData: EmailData) {
        sharingInteractor.openSupport(emailData)
    }
}