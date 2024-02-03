package a.sboev.ru.playlistmaker.audioplayer.domain.api

import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl

interface PlayerInterActor {
    fun play()
    fun pause()
    fun release()
    fun prepare(trackUrl: TrackUrl)
    fun getTrackCurrentPosition(): String
}
