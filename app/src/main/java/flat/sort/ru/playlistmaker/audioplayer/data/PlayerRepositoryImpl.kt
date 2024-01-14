package flat.sort.ru.playlistmaker.audioplayer.data

import flat.sort.ru.playlistmaker.audioplayer.data.models.TrackUrlDto
import flat.sort.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import flat.sort.ru.playlistmaker.audioplayer.domain.models.TrackUrl

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

    override fun getPosition(): Int = player.getPosition()


}