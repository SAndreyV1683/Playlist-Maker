package a.sboev.ru.playlistmaker.library.data.db.dao

import a.sboev.ru.playlistmaker.library.data.db.entity.PlayListEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlayListEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlayListEntity>

}