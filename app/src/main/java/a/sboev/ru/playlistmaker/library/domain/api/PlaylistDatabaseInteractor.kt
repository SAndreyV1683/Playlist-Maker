package a.sboev.ru.playlistmaker.library.domain.api

import a.sboev.ru.playlistmaker.library.data.db.entity.PlayListEntity
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistDatabaseInteractor {
    suspend fun insertPlaylist(playlist: Playlist): Flow<Boolean>
    suspend fun getPlaylists(): Flow<List<Playlist>>
    suspend fun insertTrackToPlayList(playlistId: Long, track: Track)
    suspend fun getPlaylistById(playlistId: Long): Playlist
    suspend fun getPlaylistTracks(tracksIdList:List<Long>): Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist)
    suspend fun deletePlayListEntity(playlist: Playlist): Flow<Boolean>
    suspend fun updatePlayListEntity(playlist: Playlist): Flow<Boolean>
}