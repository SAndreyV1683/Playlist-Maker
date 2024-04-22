package a.sboev.ru.playlistmaker.library.data.converter

import a.sboev.ru.playlistmaker.library.data.db.entity.TrackEntity
import a.sboev.ru.playlistmaker.search.domain.models.Track

class TrackDbConverter {

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
            trackName = trackEntity.trackName,
            artistName = trackEntity.artistName,
            trackTimeMillis = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100,
            previewUrl = trackEntity.previewUrl,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            timeToAddToFavorites = trackEntity.timeToAddToFavorites
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            previewUrl = track.previewUrl,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            timeToAddToFavorites = track.timeToAddToFavorites
        )
    }

}