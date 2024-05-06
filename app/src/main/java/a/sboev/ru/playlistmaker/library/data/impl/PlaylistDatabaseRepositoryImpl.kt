package a.sboev.ru.playlistmaker.library.data.impl

import a.sboev.ru.playlistmaker.library.data.converter.PlaylistDbConverter
import a.sboev.ru.playlistmaker.library.data.db.AppDatabase
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseRepository

class PlaylistDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
): PlaylistDatabaseRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun getPlaylists(): List<Playlist> {
        val entityList = appDatabase.playlistDao().getPlaylists()
        return entityList.map { playlistDbConverter.map(it) }
    }
}