package a.sboev.ru.playlistmaker.settings.domain.api

import a.sboev.ru.playlistmaker.settings.domain.model.EmailData

interface SharingInteractor {
    fun shareApp(link: String)
    fun openTerms(link: String)
    fun openSupport(emailData: EmailData)
}