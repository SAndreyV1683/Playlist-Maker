package a.sboev.ru.playlistmaker.network

import a.sboev.ru.playlistmaker.models.Track

data class TrackResponse(
    val resultCount: Int,
    val results: List<Track>
)
