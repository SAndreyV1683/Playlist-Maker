package a.sboev.ru.playlistmaker.search.data.local

import a.sboev.ru.playlistmaker.App
import a.sboev.ru.playlistmaker.search.data.History
import a.sboev.ru.playlistmaker.search.data.dto.TrackDto
import android.content.SharedPreferences
import com.google.gson.Gson
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.content.Context

const val TRACKS_KEY = "tracks_key"
class SearchHistoryHandler(private val sharedPreferences: SharedPreferences): History {

    override fun read(): List<TrackDto> {
        val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
        return Gson().fromJson(json, Array<TrackDto>::class.java).toMutableList()
    }

    override fun write(trackList: List<TrackDto>) {
        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit().remove(TRACKS_KEY).apply()
    }
}

