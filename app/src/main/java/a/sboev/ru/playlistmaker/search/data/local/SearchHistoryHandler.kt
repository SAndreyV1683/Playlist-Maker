package a.sboev.ru.playlistmaker.search.data.local

import a.sboev.ru.playlistmaker.search.data.History
import a.sboev.ru.playlistmaker.search.data.dto.TrackDto
import android.content.SharedPreferences
import com.google.gson.Gson
import org.koin.java.KoinJavaComponent.inject

private const val TRACKS_KEY = "tracks_key"
class SearchHistoryHandler(private val sharedPreferences: SharedPreferences): History {

    private val gson: Gson by inject(Gson::class.java)
    override fun read(): List<TrackDto> {
        val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
        return gson.fromJson(json, Array<TrackDto>::class.java).toMutableList()
    }

    override fun write(trackList: List<TrackDto>) {
        val json = gson.toJson(trackList)
        sharedPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit().remove(TRACKS_KEY).apply()
    }
}

