package flat.sort.ru.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import flat.sort.ru.playlistmaker.databinding.ActivityAudioPlayerBinding
import flat.sort.ru.playlistmaker.models.Track
import flat.sort.ru.playlistmaker.utils.getDuration
import flat.sort.ru.playlistmaker.utils.getYear
import flat.sort.ru.playlistmaker.utils.isNightMode
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var playerState = STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private val playbackRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING && mediaPlayer.currentPosition > 0) {
                binding.playbackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIMER_DELAY)
            }
        }
    }
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAudioPlayer.setNavigationOnClickListener { finish() }
        val intent = intent
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(BUNDLE_KEY, Track::class.java)
        } else {
            intent?.getParcelableExtra(BUNDLE_KEY)
        }
        Log.d(TAG, track.toString())
        initializeFields(track)
    }

    override fun onDestroy() {
        super.onDestroy()
        playerState = STATE_DEFAULT
        handler.removeCallbacks(playbackRunnable)
        mediaPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun initializeFields(track: Track?) {
        binding.trackName.text = track?.trackName
        binding.artistName.text = track?.artistName
        binding.durationValue.text = track?.getDuration()
        if (track?.collectionName.isNullOrEmpty()) {
            binding.collectionNameTitle.visibility = View.GONE
            binding.collectionNameValue.visibility = View.GONE
        } else {
            binding.collectionNameTitle.visibility = View.VISIBLE
            binding.collectionNameValue.visibility = View.VISIBLE
            binding.collectionNameValue.text = track?.collectionName
        }

        binding.yearValue.text = track?.getYear()
        binding.genreValue.text = track?.primaryGenreName
        binding.countryValue.text = track?.country
        Glide.with(this)
            .load(track?.artworkUrl512)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.cover)
        binding.cover.clipToOutline = true
        preparePlayer(track)
    }

    private fun preparePlayer(track: Track?) {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.ic_button_play)
            binding.playbackDuration.text = getString(R.string._0_00)
            playerState = STATE_PREPARED
            handler.removeCallbacks(playbackRunnable)
        }

        binding.playButton.setOnClickListener {
            playbackControl()
        }
    }


    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.ic_pause_button)
        handler.postDelayed(playbackRunnable, TIMER_DELAY)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.ic_button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


    companion object {
        private val TAG = AudioPlayerActivity::class.simpleName
        const val BUNDLE_KEY = "track"
        private const val TIMER_DELAY = 500L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}