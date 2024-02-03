package a.sboev.ru.playlistmaker.creators

import a.sboev.ru.playlistmaker.audioplayer.data.Player
import a.sboev.ru.playlistmaker.audioplayer.data.PlayerRepositoryImpl
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerInterActor
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import a.sboev.ru.playlistmaker.audioplayer.domain.impl.PlayerInterActorImpl

object PlayerInteractorCreator {
    private fun getPlayerRepository(player: Player): PlayerRepository {
        return PlayerRepositoryImpl(player)
    }
    fun providePlayerInteractor(player: Player): PlayerInterActor {
        return PlayerInterActorImpl(getPlayerRepository(player))
    }
}