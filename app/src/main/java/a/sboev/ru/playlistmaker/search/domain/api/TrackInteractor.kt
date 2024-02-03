package a.sboev.ru.playlistmaker.search.domain.api

import a.sboev.ru.playlistmaker.search.domain.models.Track

interface TrackInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}