package flat.sort.ru.playlistmaker.audioplayer.domain.api

import flat.sort.ru.playlistmaker.audioplayer.domain.models.TrackUrl

interface PlayerRepository {
    fun start()
    fun pause()
    fun prepare(trackUrl: TrackUrl)
    fun release()
    fun getPosition(): String
    interface StateListener {
        fun state(state: Int)
    }
}