package a.sboev.ru.playlistmaker.library.domain.impl

import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseRepository
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistDatabaseInteractorImpl(
    private val playlistDatabaseRepository: PlaylistDatabaseRepository
): PlaylistDatabaseInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistDatabaseRepository.insertPlaylist(playlist)
    }
    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDatabaseRepository.getPlaylists()
    }
    override suspend fun insertTrackToPlayList(playlistId: Long, track: Track) {
        playlistDatabaseRepository.insertTrackToPlaylist(playlistId, track)
    }
}