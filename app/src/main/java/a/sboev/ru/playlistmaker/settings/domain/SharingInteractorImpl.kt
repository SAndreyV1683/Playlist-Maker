package a.sboev.ru.playlistmaker.settings.domain

import a.sboev.ru.playlistmaker.settings.data.sharing.ExternalNavigator
import a.sboev.ru.playlistmaker.settings.domain.api.SharingInteractor
import a.sboev.ru.playlistmaker.settings.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp(link: String) {
        externalNavigator.shareLink(link)
    }

    override fun openTerms(link: String) {
        externalNavigator.openLink(link)
    }

    override fun openSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }
}