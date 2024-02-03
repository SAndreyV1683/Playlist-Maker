package a.sboev.ru.playlistmaker.audioplayer.data

import a.sboev.ru.playlistmaker.audioplayer.data.models.TrackUrlDto

interface Player {
    fun preparePlayer(trackUrl: TrackUrlDto)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getPosition(): Int
}