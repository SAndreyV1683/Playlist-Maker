package flat.sort.ru.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import flat.sort.ru.playlistmaker.models.Track

const val TRACKS_KEY = "tracks_key"
class SearchHistory(
    private val sharedPreferences: SharedPreferences
) {
    fun read(): MutableList<Track> {
        val json = sharedPreferences.getString(TRACKS_KEY, null) ?: return mutableListOf()
        return Gson().fromJson(json, Array<Track>::class.java).toMutableList()
    }

    fun write(trackList: MutableList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString(TRACKS_KEY, json)
            .apply()
    }

    fun clear() {
        sharedPreferences.edit().remove(TRACKS_KEY).apply()
    }
}

