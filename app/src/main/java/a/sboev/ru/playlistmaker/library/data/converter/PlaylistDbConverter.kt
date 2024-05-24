package a.sboev.ru.playlistmaker.library.data.converter

import a.sboev.ru.playlistmaker.library.data.db.entity.PlayListEntity
import a.sboev.ru.playlistmaker.library.data.db.entity.PlaylistsTrackEntity
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.search.domain.models.Track
import com.google.gson.Gson

class PlaylistDbConverter(private val gson: Gson) {
    fun map(playlist: Playlist): PlayListEntity {
        val json = gson.toJson(playlist.tracksIdList)
        return PlayListEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            uri = playlist.uri,
            tracksIdList = json,
            tracksCount = playlist.tracksCount
        )
    }

    fun map(playListEntity: PlayListEntity): Playlist {
        val trackList = gson.fromJson(playListEntity.tracksIdList, Array<Long>::class.java).toMutableList()
        return Playlist(
            id = playListEntity.id,
            name = playListEntity.name,
            description = playListEntity.description,
            uri = playListEntity.uri,
            tracksIdList = trackList,
            tracksCount = playListEntity.tracksCount
        )
    }

    fun map(track: Track): PlaylistsTrackEntity {
        return PlaylistsTrackEntity(
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
            trackInsertedTime = track.timeToAddToFavorites
        )
    }

    fun map(playlistsTrackEntity: PlaylistsTrackEntity): Track {
        return Track(
            trackId = playlistsTrackEntity.trackId,
            trackName = playlistsTrackEntity.trackName,
            artistName = playlistsTrackEntity.artistName,
            trackTimeMillis = playlistsTrackEntity.trackTimeMillis,
            artworkUrl100 = playlistsTrackEntity.artworkUrl100,
            previewUrl = playlistsTrackEntity.previewUrl,
            collectionName = playlistsTrackEntity.collectionName,
            releaseDate = playlistsTrackEntity.releaseDate,
            primaryGenreName = playlistsTrackEntity.primaryGenreName,
            country = playlistsTrackEntity.country,
            timeToAddToFavorites = playlistsTrackEntity.trackInsertedTime
        )
    }
}