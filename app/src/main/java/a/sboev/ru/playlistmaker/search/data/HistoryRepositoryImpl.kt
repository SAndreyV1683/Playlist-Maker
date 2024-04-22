package a.sboev.ru.playlistmaker.search.data

import a.sboev.ru.playlistmaker.search.data.dto.TrackDto
import a.sboev.ru.playlistmaker.search.domain.api.HistoryRepository
import a.sboev.ru.playlistmaker.search.domain.models.Track

class HistoryRepositoryImpl(private val historyHandler: History): HistoryRepository  {
    override fun read(): List<Track> {
        val trackDtoList = historyHandler.read()
        return trackDtoList.map {
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
    }

    override fun write(trackList: List<Track>) {
        val trackDtoList = trackList.map {
            TrackDto(
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
        historyHandler.write(trackDtoList)
    }

    override fun clear() {
        historyHandler.clear()
    }
}