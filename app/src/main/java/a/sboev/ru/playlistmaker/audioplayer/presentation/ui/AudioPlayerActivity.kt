package a.sboev.ru.playlistmaker.audioplayer.presentation.ui

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.audioplayer.presentation.AudioPlayerViewModel
import a.sboev.ru.playlistmaker.audioplayer.presentation.PlayerState
import a.sboev.ru.playlistmaker.databinding.ActivityAudioPlayerBinding
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.utils.getDuration
import a.sboev.ru.playlistmaker.utils.getYear
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide




class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding
    private var playerState: PlayerState = PlayerState.Default
    private val playbackRunnable = object : Runnable {
        override fun run() {
            if (playerState is PlayerState.Playing) {
                binding.playbackDuration.text = viewModel.getTrackCurrentPosition()
                handler.postDelayed(this, TIMER_DELAY)
            } else {
                handler.removeCallbacks(this)
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
        viewModel = ViewModelProvider(this,
            AudioPlayerViewModel.getViewModelProviderFactory(track)
        )[AudioPlayerViewModel::class.java]
        observe()
        initializeFields(track)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(playbackRunnable)
        viewModel.releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
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
        binding.playButton.setOnClickListener {
            Log.d(TAG, "Play button pressed")
            viewModel.playbackControl()
        }
        binding.yearValue.text = track?.getYear()
        binding.genreValue.text = track?.primaryGenreName
        binding.countryValue.text = track?.country
        Glide.with(this)
            .load(track?.artworkUrl512)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.cover)
        binding.cover.clipToOutline = true
    }

    private fun observe() {
        viewModel.observePlayerState().observe(this) { state ->
            Log.d(TAG, "current state $state")
            render(state)
            playerState = state
        }
    }

    private fun render(state: PlayerState) {
        when (state) {
            PlayerState.Prepared -> playerPrepared()
            PlayerState.Playing -> playerPlaying()
            PlayerState.Paused -> playerPaused()
            PlayerState.Default -> binding.playButton.isEnabled = false
        }
    }

    private fun playerPrepared() {
        binding.playButton.isEnabled = true
        binding.playButton.setImageResource(R.drawable.ic_button_play)
        binding.playbackDuration.text = getString(R.string._0_00)
        handler.removeCallbacks(playbackRunnable)
    }

    private fun playerPlaying() {
        binding.playButton.setImageResource(R.drawable.ic_pause_button)
        handler.postDelayed(playbackRunnable, TIMER_DELAY)
    }

    private fun playerPaused() {
        binding.playButton.setImageResource(R.drawable.ic_button_play)
        handler.removeCallbacks(playbackRunnable)
    }

    companion object {
        private val TAG = AudioPlayerActivity::class.simpleName
        const val BUNDLE_KEY = "track"
        private const val TIMER_DELAY = 500L
    }
}