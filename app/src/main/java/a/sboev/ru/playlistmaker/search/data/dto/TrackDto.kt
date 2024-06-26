package a.sboev.ru.playlistmaker.search.data.dto

data class TrackDto(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val timeToAddToFavorites: Long
)
