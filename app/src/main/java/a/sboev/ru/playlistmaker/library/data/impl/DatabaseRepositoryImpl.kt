package a.sboev.ru.playlistmaker.library.data.impl

import a.sboev.ru.playlistmaker.library.data.converter.TrackDbConverter
import a.sboev.ru.playlistmaker.library.data.db.AppDatabase
import a.sboev.ru.playlistmaker.library.domain.api.DatabaseRepository
import a.sboev.ru.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
): DatabaseRepository {
    override suspend fun insertTrack(track: Track) {
        appDatabase.trackDao().insertTrack(trackDbConverter.map(track))
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val entityList = appDatabase.trackDao().getTracks()
        val trackList = entityList.map {
            trackDbConverter.map(it)
        }
        emit(trackList)
    }

    override fun getTracksIdList(): Flow<List<Long>> = flow {
        val idList = appDatabase.trackDao().getTracksIdList()
        emit(idList)
    }

    override suspend fun deleteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(trackDbConverter.map(track))
    }

}