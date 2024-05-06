package a.sboev.ru.playlistmaker.library.data.converter

import a.sboev.ru.playlistmaker.library.data.db.entity.PlayListEntity
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.search.data.dto.TrackDto
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
}