package a.sboev.ru.playlistmaker.library.data.impl

import a.sboev.ru.playlistmaker.library.data.converter.PlaylistDbConverter
import a.sboev.ru.playlistmaker.library.data.db.AppDatabase
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseRepository
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PlaylistDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val gson: Gson
): PlaylistDatabaseRepository {
    override suspend fun insertPlaylist(playlist: Playlist): Flow<Boolean> = flow {
        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
        }
        emit(true)
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

    override suspend fun getPlaylistById(playlistId: Long): Playlist {
        val playlistEntity = withContext(Dispatchers.IO) {
            appDatabase.playlistDao().getPlaylistById(playlistId)
        }
        return playlistDbConverter.map(playlistEntity)
    }

    override suspend fun getPlaylistTracks(tracksIdList: List<Long>): Flow<List<Track>> = flow {
        val playlistTracks = withContext(Dispatchers.IO) {
            appDatabase.playlistDao().getTracksFromPlaylistTable()
        }
        val tracksList = mutableListOf<Track>()
        tracksIdList.forEach { id ->
            val track = playlistTracks.first { it.trackId == id }
            tracksList.add(playlistDbConverter.map(track))
        }
        emit(tracksList.asReversed())
    }

    override suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
        withContext(Dispatchers.IO) {
            val mutableTracksIdList = playlist.tracksIdList.toMutableList()
            mutableTracksIdList.remove(track.trackId)
            playlist.tracksIdList = mutableTracksIdList
            playlist.tracksCount = mutableTracksIdList.size
            appDatabase.playlistDao().updatePlaylistEntity(playlistDbConverter.map(playlist))
            if (!hasTrackInPlayLists(track))
                appDatabase.playlistDao().deleteTrackFromPlaylist(playlistDbConverter.map(track))
        }
    }

    override suspend fun deletePlayListEntity(playlist: Playlist): Flow<Boolean> = flow {
        withContext(Dispatchers.IO) {
            playlist.tracksIdList.forEach {
                appDatabase.playlistDao().deleteTrackById(it)
            }
            appDatabase.playlistDao().deletePlayListEntity(playlistDbConverter.map(playlist))
        }
        emit(true)
    }

    override suspend fun updatePlayListEntity(playlist: Playlist): Flow<Boolean> = flow {
        withContext(Dispatchers.IO) {
            appDatabase.playlistDao().updatePlaylistEntity(playlistDbConverter.map(playlist))
        }
        emit(true)
    }

    private suspend fun hasTrackInPlayLists(track: Track): Boolean {
        var hasTrack = false
        val playlists = appDatabase.playlistDao().getPlaylists()
        for (playlist in playlists) {
            if (playlist.tracksIdList.contains(track.trackId.toString())){
                hasTrack = true
                break
            }
        }
        return hasTrack
    }


}