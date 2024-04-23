package a.sboev.ru.playlistmaker.search.data

import a.sboev.ru.playlistmaker.search.data.dto.TrackResponse
import a.sboev.ru.playlistmaker.search.data.dto.TrackSearchRequest
import a.sboev.ru.playlistmaker.search.domain.api.TrackRepository
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> emit(Resource.Error("Check network connection"))
            200 -> {
                val trackResponse = (response as TrackResponse).results.map {
                    Track(
                        trackId = it.trackId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeMillis = it.trackTimeMillis,
                        artworkUrl100 = it.artworkUrl100,
                        previewUrl = it.previewUrl,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        timeToAddToFavorites = it.timeToAddToFavorites
                    )
                }
                emit(Resource.Success(trackResponse))
            }
            else -> { emit(Resource.Error("Server error")) }
        }
    }
}