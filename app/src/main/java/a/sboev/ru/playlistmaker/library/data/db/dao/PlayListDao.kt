package a.sboev.ru.playlistmaker.library.data.db.dao

import a.sboev.ru.playlistmaker.library.data.db.entity.PlayListEntity
import a.sboev.ru.playlistmaker.library.data.db.entity.PlaylistsTrackEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlayListEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlayListEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(playlistsTrack: PlaylistsTrackEntity)

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): PlayListEntity

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updatePlaylistEntity(playlist: PlayListEntity)

    @Query("SELECT * FROM playlists_track_table")
    suspend fun getTracksFromPlaylistTable(): List<PlaylistsTrackEntity>

    @Delete(entity = PlaylistsTrackEntity::class)
    suspend fun deleteTrackFromPlaylist(trackEntity: PlaylistsTrackEntity)

    @Delete(entity = PlayListEntity::class)
    suspend fun deletePlayListEntity(playlist: PlayListEntity)

    @Query("DELETE FROM playlists_track_table WHERE id = :trackId")
    suspend fun deleteTrackById(trackId: Long)

}