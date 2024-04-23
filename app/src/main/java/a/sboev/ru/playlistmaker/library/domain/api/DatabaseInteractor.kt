package a.sboev.ru.playlistmaker.library.domain.api

import a.sboev.ru.playlistmaker.search.data.dto.TrackDto
import a.sboev.ru.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface DatabaseInteractor {
    suspend fun insertTrack(track: Track)


    fun getTracks(): Flow<List<Track>>


    fun getTracksIdList(): Flow<List<Long>>


    suspend fun deleteTrack(track: Track)
}