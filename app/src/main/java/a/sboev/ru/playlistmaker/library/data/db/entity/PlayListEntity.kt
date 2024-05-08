package a.sboev.ru.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    val name: String,
    val description: String,
    val uri: String,
    var tracksIdList: String,
    var tracksCount: Int
)