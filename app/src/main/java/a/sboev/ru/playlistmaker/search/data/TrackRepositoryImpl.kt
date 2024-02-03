package a.sboev.ru.playlistmaker.search.data

import a.sboev.ru.playlistmaker.search.data.dto.TrackResponse
import a.sboev.ru.playlistmaker.search.data.dto.TrackSearchRequest
import a.sboev.ru.playlistmaker.search.domain.api.TrackRepository
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.utils.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient): TrackRepository {
    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> Resource.Error("Check network connection")
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
                        country = it.country
                    )
                }
                Resource.Success(trackResponse)
            }
            else -> { Resource.Error("Server error") }
        }
    }
}