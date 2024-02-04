package a.sboev.ru.playlistmaker.search.domain.api

import a.sboev.ru.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun read(): List<Track>

    fun write(trackList: List<Track>)

    fun clear()
}