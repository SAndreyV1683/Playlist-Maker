package a.sboev.ru.playlistmaker.library.domain.models

data class Playlist(
    val id: Long?,
    val name: String,
    val description: String,
    val uri: String,
    var tracksIdList: List<Long>,
    var tracksCount: Int
)
