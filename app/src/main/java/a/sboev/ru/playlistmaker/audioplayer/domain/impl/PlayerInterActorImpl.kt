package a.sboev.ru.playlistmaker.audioplayer.domain.impl

import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerInterActor
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl

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