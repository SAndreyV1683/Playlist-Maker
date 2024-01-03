package flat.sort.ru.playlistmaker.network

import flat.sort.ru.playlistmaker.models.Track

data class TrackResponse(
    val resultCount: Int,
    val results: List<Track>
)
