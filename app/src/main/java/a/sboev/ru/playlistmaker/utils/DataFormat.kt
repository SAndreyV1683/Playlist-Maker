package a.sboev.ru.playlistmaker.utils

import a.sboev.ru.playlistmaker.models.Track
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

fun Track.getYear(): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = releaseDate?.let { format.parse(it) }
    val myCal: Calendar = GregorianCalendar()
    date?.let {myCal.time = date }
    return myCal.get(Calendar.YEAR).toString()
}
fun Track.getDuration(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)