package a.sboev.ru.playlistmaker.library.domain.models

data class Playlist(
    val id: Long?,
    var name: String,
    var description: String,
    var uri: String,
    var tracksIdList: List<Long>,
    var tracksCount: Int
)
