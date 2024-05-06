package a.sboev.ru.playlistmaker.library.domain.api

import a.sboev.ru.playlistmaker.library.domain.models.Playlist

interface PlaylistDatabaseInteractor {
    suspend fun insertPlaylist(playlist: Playlist)
}