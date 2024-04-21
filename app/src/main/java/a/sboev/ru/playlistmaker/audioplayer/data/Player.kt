package a.sboev.ru.playlistmaker.audioplayer.data

import a.sboev.ru.playlistmaker.audioplayer.data.models.PlayerStateDto
import a.sboev.ru.playlistmaker.audioplayer.data.models.TrackUrlDto
import kotlinx.coroutines.flow.Flow

interface Player {
    fun preparePlayer(trackUrl: TrackUrlDto)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getPlayerState(): Flow<PlayerStateDto>
}