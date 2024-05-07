package a.sboev.ru.playlistmaker.library.domain.api

import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistDatabaseInteractor {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
}