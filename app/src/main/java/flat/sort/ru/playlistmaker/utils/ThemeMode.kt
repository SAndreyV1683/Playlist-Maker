package flat.sort.ru.playlistmaker.utils


import android.content.res.Configuration
import flat.sort.ru.playlistmaker.App

fun isNightMode(): Boolean {
    val nightModeFlags =
        App.INSTANCE.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
}