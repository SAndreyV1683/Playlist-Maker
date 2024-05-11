package a.sboev.ru.playlistmaker.library.data.impl

import a.sboev.ru.playlistmaker.library.data.converter.PlaylistDbConverter
import a.sboev.ru.playlistmaker.library.data.db.AppDatabase
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseRepository
import a.sboev.ru.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PlaylistDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val gson: Gson
): PlaylistDatabaseRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val entityList = appDatabase.playlistDao().getPlaylists()
        emit(entityList.map { playlistDbConverter.map(it) })
    }

    override suspend fun insertTrackToPlaylist(playlistId: Long, track: Track) {
        appDatabase.playlistDao().insertTrackToPlaylist(playlistDbConverter.map(track))
        val playlistEntity = appDatabase.playlistDao().getPlaylistById(playlistId)
        val tracksIdList = gson.fromJson(playlistEntity.tracksIdList, Array<Long>::class.java).toMutableList()
        tracksIdList.add(track.trackId)
        val trackListJson = gson.toJson(tracksIdList)
        playlistEntity.tracksCount = tracksIdList.size
        playlistEntity.tracksIdList = trackListJson
        appDatabase.playlistDao().updatePlaylistEntity(playlistEntity)
    }
}