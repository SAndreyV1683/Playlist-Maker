package a.sboev.ru.playlistmaker.library.ui.playlistinfo.models

data class PlaylistInfo(
    val name: String,
    val description: String,
    val uri: String,
    val tracksDuration: String,
    val tracksCount: Int
)
