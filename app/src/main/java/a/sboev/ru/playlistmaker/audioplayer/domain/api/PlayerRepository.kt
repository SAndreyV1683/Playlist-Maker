package a.sboev.ru.playlistmaker.audioplayer.domain.api

import a.sboev.ru.playlistmaker.audioplayer.domain.models.PlayerState
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun start()
    fun pause()
    fun prepare(trackUrl: TrackUrl)
    fun release()
    fun getPlayerState(): Flow<PlayerState>

}