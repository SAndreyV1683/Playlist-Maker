package a.sboev.ru.playlistmaker.search.data

import a.sboev.ru.playlistmaker.search.data.dto.TrackDto
import a.sboev.ru.playlistmaker.search.domain.models.Track

interface History {
    fun read(): List<TrackDto>

    fun write(trackList: List<TrackDto>)

    fun clear()
}