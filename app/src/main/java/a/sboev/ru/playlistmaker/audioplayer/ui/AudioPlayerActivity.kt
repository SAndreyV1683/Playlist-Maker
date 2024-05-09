package a.sboev.ru.playlistmaker.audioplayer.ui

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.audioplayer.presentation.AudioPlayerViewModel
import a.sboev.ru.playlistmaker.audioplayer.presentation.PlayerStateUi
import a.sboev.ru.playlistmaker.databinding.ActivityAudioPlayerBinding
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.library.ui.newplaylist.NewPlaylistFragment
import a.sboev.ru.playlistmaker.library.ui.newplaylist.adapters.PlaylistItemHorizontalAdapter
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.utils.getDuration
import a.sboev.ru.playlistmaker.utils.getYear
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(track)
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var binding: ActivityAudioPlayerBinding
    private val onItemClickListener = PlaylistItemHorizontalAdapter.OnItemClickListener { playlist ->
        if (playlist.id != null) {
            val trackIdsList = playlist.tracksIdList
            if (trackIdsList.contains(track.trackId)) {
                Toast.makeText(this, getString(R.string.already_been_added_to_the_playlist, track.trackName), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertTrackToPlaylist(playlist.id, track)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                Toast.makeText(this, getString(R.string.track_added_to_playlist_text, track.trackName), Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val adapter = PlaylistItemHorizontalAdapter(onItemClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAudioPlayer.setNavigationOnClickListener { finish() }
        binding.bottomSheetRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.bottomSheetRv.adapter = adapter
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
        binding.favButton.setOnClickListener {
            track?.let {
                viewModel.addTrackToFavorites(track)
            }
        }
        viewModel.checkTackIsOnFavorites()
        viewModel.observeFavButtonState().observe(this) { isSelected ->
            binding.favButton.isSelected = isSelected
        }
        viewModel.observeState().observe(this) { state ->
            when (state) {
                is LibState.Loading -> {}
                is LibState.Empty -> showErrorMessage()
                is LibState.Content<*> -> {
                    val list: List<Playlist> = state.list as List<Playlist>
                    showContent(list)
                }
            }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.addButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.newPlaylist.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, NewPlaylistFragment())
                commit()
            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylists()
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

    private fun showErrorMessage() {
        binding.apply {
            bottomSheetRv.isVisible = false
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        adapter.playlists = playlists.toMutableList()
        adapter.refresh()
        binding.apply {
            bottomSheetRv.isVisible = true
        }
    }

    companion object {
        private val TAG = AudioPlayerActivity::class.simpleName
        const val BUNDLE_KEY = "track"
    }
}