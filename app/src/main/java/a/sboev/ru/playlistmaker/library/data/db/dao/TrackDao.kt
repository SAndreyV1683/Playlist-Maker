package a.sboev.ru.playlistmaker.library.data.db.dao

import a.sboev.ru.playlistmaker.library.data.db.entity.TrackEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM track_table")
    suspend fun getTracksIdList(): List<Long>

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

}