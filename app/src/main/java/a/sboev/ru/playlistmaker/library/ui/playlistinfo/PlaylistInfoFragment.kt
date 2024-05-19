package a.sboev.ru.playlistmaker.library.ui.playlistinfo

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.audioplayer.ui.AudioPlayerActivity
import a.sboev.ru.playlistmaker.databinding.FragmentPlaylistInfoBinding
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.PlaylistInfoViewModel
import a.sboev.ru.playlistmaker.library.ui.BindingFragment
import a.sboev.ru.playlistmaker.library.ui.playlistinfo.models.PlaylistInfo
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.search.ui.adapters.TrackAdapter
import a.sboev.ru.playlistmaker.utils.debounce
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment: BindingFragment<FragmentPlaylistInfoBinding>() {

    private var playlistId = 0L
    private val viewModel by viewModel<PlaylistInfoViewModel> {
        parametersOf(playlistId)
    }
    private lateinit var onItemClickDebounce: (Track) -> Unit

    private val onClickListener = object : TrackAdapter.OnItemClickListener {
        override fun onLongClick(track: Track) {
            showDialog(track)
        }

        override fun onClick(track: Track) {
            onItemClickDebounce(track)
        }

    }
    private val adapter = TrackAdapter(onClickListener)
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlaylistInfoBinding {
        return FragmentPlaylistInfoBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = requireArguments().getLong(PLAYLIST_ID)
        onItemClickDebounce = debounce(DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(AudioPlayerActivity.BUNDLE_KEY, track)
            this.startActivity(intent)
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.bottomSheetRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.bottomSheetRv.adapter = adapter
        viewModel.getPlaylistTracks()
        viewModel.observeInfoState().observe(viewLifecycleOwner) { playlistInfo ->
            playlistInfo?.let { renderInfo(it) }
        }
        viewModel.observeTracksState().observe(viewLifecycleOwner) {
            when (it) {
                is LibState.Loading -> {}
                is LibState.Empty -> hideContent()
                is LibState.Content<*> -> {
                    val list: List<Track> = it.list as List<Track>
                    showContent(list)
                }
            }
        }
        binding.shareButton.setOnClickListener {
            viewModel.sharePlaylist(formShareString())
        }
        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.menuButton.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.menuBottomSheetShareTv.setOnClickListener {
            viewModel.sharePlaylist(formShareString())
        }
    }


    private fun renderInfo(playlistInfo: PlaylistInfo) {
        binding.apply {
            playlistNameTv.text = playlistInfo.name
            playlistDescriptionTv.text = playlistInfo.description
            var durationAndCount = when (playlistInfo.tracksDuration.toInt()) {
                0,5 -> getString(R.string.tracks_duration, playlistInfo.tracksDuration)
                1 -> getString(R.string.one_minute_tacks_duration, playlistInfo.tracksDuration)
                2,3,4 -> getString(R.string.less_than_5_minutes_tacks_duration, playlistInfo.tracksDuration)
                else -> getString(R.string.tracks_duration, playlistInfo.tracksDuration)
            }
            val tracksCount = when (playlistInfo.tracksCount) {
                0,5 -> getString(R.string.playlist_tracks_count, playlistInfo.tracksCount.toString())
                1 -> getString(R.string.playlist_one_track_count)
                2,3,4 -> getString(R.string.playlist_less_than_5_tracks_count, playlistInfo.tracksCount.toString())
                else -> getString(R.string.playlist_tracks_count, playlistInfo.tracksCount.toString())
            }
            durationAndCount += tracksCount

            playlistTracksCountAndDuration.text = durationAndCount
            Glide.with(requireContext())
                .load(playlistInfo.uri)
                .placeholder(R.drawable.ic_placeholder)
                .into(playlistCoverIv)
            menuBottomSheetPlaylistCover.setImageURI(playlistInfo.uri.toUri())
            menuBottomSheetPlaylistTracksCount.text = tracksCount
            menuBottomSheetPlaylistName.text = playlistInfo.name
        }
    }

    private fun showContent(playlists: List<Track>) {
        adapter.tracks = playlists.toMutableList()
        adapter.refresh()
        binding.apply {
            bottomSheetRv.isVisible = true
        }
    }

    private fun hideContent() {
        binding.bottomSheetRv.isVisible = false
    }

    private fun showDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext(), R.style.alert_dialog_style)
            .setMessage(R.string.track_delete_dialog_message)
            .setPositiveButton(getString(R.string.dialog_positive_button_text)) { _, _ ->
                viewModel.deleteTrackFromPlayList(track)
            }
            .setNegativeButton(getString(R.string.dialog_negative_button_text)) { _, _ ->

            }
            .setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.dialog_background))
            .show()
    }

    private fun formShareString(): String {
        var shareString = "${viewModel.playlist.name}\n" +
                "${viewModel.playlist.description}\n" +
                "${when (viewModel.playlist.tracksCount) {
                    0,5 -> getString(R.string.playlist_tracks_count, viewModel.playlist.tracksCount.toString())
                    1 -> getString(R.string.playlist_one_track_count)
                    2,3,4 -> getString(R.string.playlist_less_than_5_tracks_count, viewModel.playlist.tracksCount.toString())
                    else -> getString(R.string.playlist_tracks_count, viewModel.playlist.tracksCount.toString())}}\n"
        var tracksString = ""
        val tracksList = viewModel.tracks
        tracksList.forEachIndexed { index, track ->
            tracksString += "${index + 1}. ${track.artistName} - ${track.trackName}\n"
        }
        shareString += tracksString
        return shareString
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
        private const val PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long): Bundle = bundleOf(
            PLAYLIST_ID to playlistId
        )
    }
}