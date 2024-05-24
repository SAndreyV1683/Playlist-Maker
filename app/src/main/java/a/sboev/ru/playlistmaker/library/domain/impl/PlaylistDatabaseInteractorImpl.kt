package a.sboev.ru.playlistmaker.library.domain.impl

import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseRepository
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistDatabaseInteractorImpl(
    private val playlistDatabaseRepository: PlaylistDatabaseRepository
): PlaylistDatabaseInteractor {
    override suspend fun insertPlaylist(playlist: Playlist): Flow<Boolean> {
        return playlistDatabaseRepository.insertPlaylist(playlist)
    }
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDatabaseRepository.getPlaylists()
    }
    override suspend fun insertTrackToPlayList(playlistId: Long, track: Track) {
        playlistDatabaseRepository.insertTrackToPlaylist(playlistId, track)
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist {
        return playlistDatabaseRepository.getPlaylistById(playlistId)
    }

    override suspend fun getPlaylistTracks(tracksIdList: List<Long>): Flow<List<Track>> {
        return playlistDatabaseRepository.getPlaylistTracks(tracksIdList)
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        playlistDatabaseRepository.deleteTrackFromPlaylist(track, playlist)
    }

    override suspend fun deletePlayListEntity(playlist: Playlist): Flow<Boolean> {
        return playlistDatabaseRepository.deletePlayListEntity(playlist)
    }

    override suspend fun updatePlayListEntity(playlist: Playlist): Flow<Boolean> {
        return playlistDatabaseRepository.updatePlayListEntity(playlist)
    }
}