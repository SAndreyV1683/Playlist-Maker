package flat.sort.ru.playlistmaker.audioplayer.presentation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import flat.sort.ru.playlistmaker.R
import flat.sort.ru.playlistmaker.audioplayer.data.MyMediaPlayer
import flat.sort.ru.playlistmaker.audioplayer.data.PlayerRepositoryImpl
import flat.sort.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import flat.sort.ru.playlistmaker.audioplayer.domain.impl.PlayerInterActorImpl
import flat.sort.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import flat.sort.ru.playlistmaker.databinding.ActivityAudioPlayerBinding
import flat.sort.ru.playlistmaker.models.Track
import flat.sort.ru.playlistmaker.utils.getDuration
import flat.sort.ru.playlistmaker.utils.getYear

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private var playerState = STATE_DEFAULT
    private val playerStateListener = object : PlayerRepository.StateListener {
        override fun state(state: Int) {
            playerState = state
            if (playerState == STATE_PREPARED) {
                binding.playButton.isEnabled = true
                binding.playButton.setImageResource(R.drawable.ic_button_play)
                binding.playbackDuration.text = getString(R.string._0_00)
            }
        }
    }
    private val playerInterActorImpl = PlayerInterActorImpl(PlayerRepositoryImpl(MyMediaPlayer(playerStateListener)))
    private val playbackRunnable = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                binding.playbackDuration.text = playerInterActorImpl.getTrackCurrentPosition()
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
        playerInterActorImpl.release()
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
        val previewUrl = track?.previewUrl
        if (previewUrl != null) {
            playerInterActorImpl.prepare(TrackUrl(previewUrl))
        }
        binding.playButton.setOnClickListener {
            playbackControl()
        }
    }


    private fun startPlayer() {
        playerInterActorImpl.play()
        binding.playButton.setImageResource(R.drawable.ic_pause_button)
        handler.postDelayed(playbackRunnable, TIMER_DELAY)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        playerInterActorImpl.pause()
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