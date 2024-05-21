package a.sboev.ru.playlistmaker.library.ui.playlist


import a.sboev.ru.playlistmaker.R
import a.sboev.ru.playlistmaker.databinding.FragmentPlayListsBinding
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.library.ui.BindingFragment
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.PlayListsViewModel
import a.sboev.ru.playlistmaker.library.ui.newplaylist.adapters.PlaylistItemAdapter
import a.sboev.ru.playlistmaker.library.ui.playlistinfo.PlaylistInfoFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment: BindingFragment<FragmentPlayListsBinding>() {

    private val viewModel by viewModel<PlayListsViewModel>()
    private lateinit var adapter: PlaylistItemAdapter
    private val itemClickListener = object: PlaylistItemAdapter.ItemClickListener {
        override fun onClick(playList: Playlist) {
            if (playList.id != null)
                findNavController().navigate(R.id.action_libraryFragment_to_playlistInfoFragment, PlaylistInfoFragment.createArgs(playList.id))
        }
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayListsBinding {
        return FragmentPlayListsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playlistsRv.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = PlaylistItemAdapter(itemClickListener)
        binding.playlistsRv.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is LibState.Loading -> {}
                is LibState.Empty -> showErrorMessage()
                is LibState.Content<*> -> {
                    val list: List<Playlist> = state.list as List<Playlist>
                    showContent(list)
                }
            }
        }
        binding.playlistsButton.setOnClickListener {
             findNavController().navigate(R.id.action_libraryFragment_to_newPlaylistFragment)
        }

    }

    private fun showErrorMessage() {
        adapter.playlists.clear()
        adapter.refresh()
        binding.apply {
            playlistsErrorImageView.isVisible = true
            playlistsErrorTextView.isVisible = true
            playlistsButton.isVisible = true
            playlistsRv.isVisible = false
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        adapter.playlists = playlists.toMutableList()
        adapter.refresh()
        binding.apply {
            playlistsErrorImageView.isVisible = false
            playlistsErrorTextView.isVisible = false
            playlistsButton.isVisible = true
            playlistsRv.isVisible = true
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
    }

    companion object {
        fun newInstance() = PlayListsFragment()
    }

}