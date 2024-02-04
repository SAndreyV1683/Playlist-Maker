package a.sboev.ru.playlistmaker.settings.presentation

import a.sboev.ru.playlistmaker.App
import a.sboev.ru.playlistmaker.creators.SharingInteractorCreator
import a.sboev.ru.playlistmaker.settings.domain.ThemeSettings
import a.sboev.ru.playlistmaker.settings.domain.model.EmailData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    private val settingsInteractor = App.INSTANCE.settingsInteractor
    private val sharingInteractor = SharingInteractorCreator.provideSharingInteractor()

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
    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }
}