package a.sboev.ru.playlistmaker.library.domain.api

import a.sboev.ru.playlistmaker.library.domain.models.Playlist

interface PlaylistDatabaseRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun getPlaylists(): List<Playlist>

}