package flat.sort.ru.playlistmaker.audioplayer.data

import flat.sort.ru.playlistmaker.audioplayer.data.models.TrackUrlDto
import flat.sort.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import flat.sort.ru.playlistmaker.audioplayer.domain.models.TrackUrl
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

    override fun getPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(player.getPosition())
    }
}