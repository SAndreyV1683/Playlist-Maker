package a.sboev.ru.playlistmaker.library.data.db

import a.sboev.ru.playlistmaker.library.data.db.dao.PlayListDao
import a.sboev.ru.playlistmaker.library.data.db.dao.TrackDao
import a.sboev.ru.playlistmaker.library.data.db.entity.PlayListEntity
import a.sboev.ru.playlistmaker.library.data.db.entity.PlaylistsTrackEntity
import a.sboev.ru.playlistmaker.library.data.db.entity.TrackEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [TrackEntity::class, PlayListEntity::class, PlaylistsTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlayListDao
}