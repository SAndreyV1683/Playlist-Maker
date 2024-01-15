package flat.sort.ru.playlistmaker.audioplayer.domain.impl

import flat.sort.ru.playlistmaker.audioplayer.domain.api.PlayerInterActor
import flat.sort.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import flat.sort.ru.playlistmaker.audioplayer.domain.models.TrackUrl

class PlayerInterActorImpl(private val repository: PlayerRepository): PlayerInterActor {
    override fun play() {
        repository.start()
    }

    override fun pause() {
        repository.pause()
    }

    override fun release() {
        repository.release()
    }

    override fun prepare(trackUrl: TrackUrl) {
        repository.prepare(trackUrl)
    }

    override fun getTrackCurrentPosition(): String = repository.getPosition()

}