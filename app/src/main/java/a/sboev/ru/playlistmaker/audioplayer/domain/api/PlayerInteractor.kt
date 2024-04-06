package a.sboev.ru.playlistmaker.audioplayer.domain.api

import a.sboev.ru.playlistmaker.audioplayer.domain.models.PlayerState
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import kotlinx.coroutines.flow.Flow

interface PlayerInterActor {
    fun play()
    fun pause()
    fun release()
    fun prepare(trackUrl: TrackUrl)
    fun getPlayerState(): Flow<PlayerState>
}
