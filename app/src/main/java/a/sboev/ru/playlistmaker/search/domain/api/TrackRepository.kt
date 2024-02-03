package a.sboev.ru.playlistmaker.search.domain.api

import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.utils.Resource

interface TrackRepository {
    fun searchTrack(expression: String): Resource<List<Track>>
}