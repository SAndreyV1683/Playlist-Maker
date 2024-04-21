package a.sboev.ru.playlistmaker.audioplayer.data

import a.sboev.ru.playlistmaker.audioplayer.data.models.PlayerStateDto
import a.sboev.ru.playlistmaker.audioplayer.data.models.TrackUrlDto
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import a.sboev.ru.playlistmaker.audioplayer.domain.models.PlayerState
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(private val player: Player): PlayerRepository {

    override fun start() {
        player.startPlayer()
    }

    override fun pause() {
        player.pausePlayer()
    }

    override fun prepare(trackUrl: TrackUrl) {
        val trackUrlDto = TrackUrlDto(trackUrl.url)
        player.preparePlayer(trackUrlDto)
    }

    override fun release() {
        player.release()
    }

    override fun getPlayerState(): Flow<PlayerState> {
        return player.getPlayerState().map { playerState ->
            when(playerState) {
                is PlayerStateDto.Default -> PlayerState.Default()
                is PlayerStateDto.Prepared -> PlayerState.Prepared()
                is PlayerStateDto.Playing -> {
                    val process = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerState.progress)
                    PlayerState.Playing(process)
                }
                is PlayerStateDto.Paused -> {
                    val process = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerState.progress)
                    PlayerState.Paused(process)
                }
            }
        }
    }
}