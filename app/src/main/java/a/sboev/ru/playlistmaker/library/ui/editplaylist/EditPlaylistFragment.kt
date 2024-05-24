package a.sboev.ru.playlistmaker.library.ui.editplaylist

import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.ui.newplaylist.NewPlaylistFragment
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController

class EditPlaylistFragment: NewPlaylistFragment() {

    private var playlistId: Long = 0
    private lateinit var playlist: Playlist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        binding.toolbarTitle.text = getString(R.string.edit_fragment_title_text)
        playlistId = requireArguments().getLong(PLAYLIST_ID)
        playlistViewModel.observePlaylistState().observe(viewLifecycleOwner) {
            if (it != null) {
                render(it)
                playlist = it
            }
        }
        playlistViewModel.observePlaylistUpdateState().observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().popBackStack()
            }
        }
        playlistViewModel.getPlaylistById(playlistId)
        binding.createButton.text = getString(R.string.edit_fragment_save_button_text)
        binding.createButton.setOnClickListener {
            playlist.name = playlistName
            playlist.description = playlistDescription
            playlist.uri = playlistImageUri
            playlistViewModel.updatePlaylist(playlist)
        }
        binding.newPlaylistToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun render(playlist: Playlist) {
        binding.apply {
            playlistCover.scaleType = ImageView.ScaleType.CENTER_CROP
            if (playlist.uri.isEmpty())
                playlistCover.setImageDrawable(requireContext().getDrawable(R.drawable.ic_placeholder))
            else
                playlistCover.setImageURI(playlist.uri.toUri())
            nameEdittext.text.append(playlist.name)
            descriptionEdittext.text.append(playlist.description)
        }
    }

    companion object {
        private const val PLAYLIST_ID = "playlist_id"
        fun createArgs(playlistId: Long): Bundle = bundleOf(
            PLAYLIST_ID to playlistId
        )
    }
}