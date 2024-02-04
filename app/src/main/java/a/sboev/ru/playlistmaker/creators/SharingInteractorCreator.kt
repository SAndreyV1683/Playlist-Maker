package a.sboev.ru.playlistmaker.creators

import a.sboev.ru.playlistmaker.settings.data.sharing.ExternalNavigator
import a.sboev.ru.playlistmaker.settings.domain.SharingInteractorImpl
import a.sboev.ru.playlistmaker.settings.domain.api.SharingInteractor

object SharingInteractorCreator {
    fun provideSharingInteractor(): SharingInteractor = SharingInteractorImpl(ExternalNavigator())
}