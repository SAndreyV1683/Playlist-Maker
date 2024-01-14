package flat.sort.ru.playlistmaker.audioplayer.data

import flat.sort.ru.playlistmaker.audioplayer.data.models.TrackUrlDto

interface Player {
    fun preparePlayer(trackUrl: TrackUrlDto)
    fun startPlayer()
    fun pausePlayer()
    fun release()

    fun getPosition(): Int
}