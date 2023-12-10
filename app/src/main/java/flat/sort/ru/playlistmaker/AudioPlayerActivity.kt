package flat.sort.ru.playlistmaker

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import flat.sort.ru.playlistmaker.models.Track
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private val cover: ImageView by lazy { findViewById(R.id.cover) }
    private val trackNameTextView: TextView by lazy { findViewById(R.id.track_name) }
    private val artistNameTextView: TextView by lazy { findViewById(R.id.artist_name) }
    private val durationTextView: TextView by lazy { findViewById(R.id.duration_value) }
    private val albumTextView: TextView by lazy { findViewById(R.id.collection_name_value) }
    private val albumTitleTextView: TextView by lazy { findViewById(R.id.collection_name_title) }
    private val yearTextView: TextView by lazy { findViewById(R.id.year_value) }
    private val genreTextView: TextView by lazy { findViewById(R.id.genre_value) }
    private val countryTextView: TextView by lazy { findViewById(R.id.country_value) }
    private val toolbar:Toolbar by lazy { findViewById(R.id.toolbar_audio_player)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        toolbar.setNavigationOnClickListener { finish() }
        val intent = intent
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(BUNDLE_KEY, Track::class.java)
        } else {
            intent?.getParcelableExtra(BUNDLE_KEY)
        }
        Log.d(TAG, track.toString())

        initializeFields(track)
    }

    private fun initializeFields(track: Track?) {
        trackNameTextView.text = track?.trackName
        artistNameTextView.text = track?.artistName
        durationTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track?.trackTimeMillis)
        if (track?.collectionName.isNullOrEmpty()) {
            albumTextView.visibility = View.GONE
            albumTitleTextView.visibility = View.GONE
        } else {
            albumTextView.visibility = View.VISIBLE
            albumTitleTextView.visibility = View.VISIBLE
            albumTextView.text = track?.collectionName
        }
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = track?.releaseDate?.let { format.parse(it) }
        val myCal: Calendar = GregorianCalendar()
        if (date != null) {
            myCal.time = date
        }
        yearTextView.text = myCal.get(Calendar.YEAR).toString()
        genreTextView.text = track?.primaryGenreName
        countryTextView.text = track?.country
        Glide.with(this)
            .load(track?.artworkUrl100?.let { getCoverArtwork(it) })
            .placeholder(R.drawable.ic_placeholder)
            .into(cover)
    }

    private fun getCoverArtwork(artworkUrl100: String) = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    companion object {
        val TAG = AudioPlayerActivity::class.simpleName
        const val BUNDLE_KEY = "track"
    }
}