package a.sboev.ru.playlistmaker.library.domain.api

import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistDatabaseRepository {
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun insertTrackToPlaylist(playlistId: Long, track: Track)

}