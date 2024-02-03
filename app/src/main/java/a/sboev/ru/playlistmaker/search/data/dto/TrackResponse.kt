package a.sboev.ru.playlistmaker.search.data.dto

data class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
