package a.sboev.ru.playlistmaker.library.domain.impl

import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseRepository
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
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
}