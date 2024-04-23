package a.sboev.ru.playlistmaker.library.domain.impl

import a.sboev.ru.playlistmaker.library.domain.api.DatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.api.DatabaseRepository
import a.sboev.ru.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class DatabaseInteractorImpl(
    private val databaseRepository: DatabaseRepository
): DatabaseInteractor {
    override suspend fun insertTrack(track: Track) {
        databaseRepository.insertTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return databaseRepository.getTracks()
    }

    override fun getTracksIdList(): Flow<List<Long>> {
        return databaseRepository.getTracksIdList()
    }

    override suspend fun deleteTrack(track: Track) {
        databaseRepository.deleteTrack(track)
    }
}