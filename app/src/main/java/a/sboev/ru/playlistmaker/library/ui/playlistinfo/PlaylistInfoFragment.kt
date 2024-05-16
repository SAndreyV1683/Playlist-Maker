package a.sboev.ru.playlistmaker.library.ui.playlistinfo

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.databinding.FragmentPlaylistInfoBinding
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.PlaylistInfoViewModel
import a.sboev.ru.playlistmaker.library.ui.BindingFragment
import a.sboev.ru.playlistmaker.library.ui.playlistinfo.models.PlaylistInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistInfoFragment: BindingFragment<FragmentPlaylistInfoBinding>() {

    private var playlistId = 0L
    private val viewModel by viewModel<PlaylistInfoViewModel> {
        parametersOf(playlistId)
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlaylistInfoBinding {
        return FragmentPlaylistInfoBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistId = requireArguments().getLong(PLAYLIST_ID)
        viewModel.getPlaylistTracks()
        viewModel.observeInfoState().observe(viewLifecycleOwner) { playlistInfo ->
            playlistInfo?.let { renderInfo(it) }
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
            durationAndCount += " " + when (playlistInfo.tracksCount) {
                0,5 -> getString(R.string.playlist_tracks_count, playlistInfo.tracksCount.toString())
                1 -> getString(R.string.playlist_one_track_count)
                2,3,4 -> getString(R.string.playlist_less_than_5_tracks_count, playlistInfo.tracksCount.toString())
                else -> getString(R.string.playlist_tracks_count, playlistInfo.tracksCount.toString())
            }
            playlistTracksCountAndDuration.text = durationAndCount
            Glide.with(requireContext())
                .load(playlistInfo.uri)
                .placeholder(R.drawable.ic_placeholder)
                .into(playlistCoverIv)
        }
    }

    companion object {

        private const val PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long): Bundle = bundleOf(
            PLAYLIST_ID to playlistId
        )
    }
}