package flat.sort.ru.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM


class App: Application() {

    var darkTheme = false
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAY_LIST_MAKER_SHARED_PREFS, MODE_PRIVATE)
        if (!sharedPreferences.getBoolean("isDarkTheme", darkTheme)) {
            sharedPreferences.edit().putBoolean("isDarkTheme", darkTheme).apply()
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            darkTheme = sharedPreferences.getBoolean("isDarkTheme", darkTheme)
            switchTheme(darkTheme)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPreferences.edit().putBoolean("isDarkTheme", darkTheme).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

const val PLAY_LIST_MAKER_SHARED_PREFS = "play_list_maker_shared_prefs "