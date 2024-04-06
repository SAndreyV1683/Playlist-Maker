package a.sboev.ru.playlistmaker.audioplayer.ui

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.audioplayer.presentation.AudioPlayerViewModel
import a.sboev.ru.playlistmaker.audioplayer.presentation.PlayerStateUi
import a.sboev.ru.playlistmaker.databinding.ActivityAudioPlayerBinding
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.utils.getDuration
import a.sboev.ru.playlistmaker.utils.getYear
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(track)
    }
    private lateinit var binding: ActivityAudioPlayerBinding

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
        if (track != null) {
            this.track = track
        }
        observe()
        initializeFields(track)
    }

    override fun onDestroy() {
        super.onDestroy()
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
        viewModel.observeTrackPosition().observe(this) {position ->
            binding.playbackDuration.text = position
        }
        viewModel.observePlayerState().observe(this) { state ->
            Log.d(TAG, "current state $state")
            render(state)
        }
    }

    private fun render(state: PlayerStateUi) {
        when (state) {
            PlayerStateUi.Prepared -> playerPrepared()
            PlayerStateUi.Playing -> playerPlaying()
            PlayerStateUi.Paused -> playerPaused()
            PlayerStateUi.Default -> binding.playButton.isEnabled = false
        }
    }

    private fun playerPrepared() {
        binding.playButton.isEnabled = true
        binding.playButton.setImageResource(R.drawable.ic_button_play)
        binding.playbackDuration.text = getString(R.string._0_00)
    }

    private fun playerPlaying() {
        binding.playButton.setImageResource(R.drawable.ic_pause_button)
    }

    private fun playerPaused() {
        binding.playButton.setImageResource(R.drawable.ic_button_play)
    }

    companion object {
        private val TAG = AudioPlayerActivity::class.simpleName
        const val BUNDLE_KEY = "track"
    }
}