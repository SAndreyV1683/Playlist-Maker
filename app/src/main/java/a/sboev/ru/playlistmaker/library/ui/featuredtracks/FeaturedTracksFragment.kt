package a.sboev.ru.playlistmaker.library.ui.featuredtracks

import a.sboev.ru.playlistmaker.audioplayer.ui.AudioPlayerActivity
import a.sboev.ru.playlistmaker.databinding.FragmentFeaturedTracksBinding
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.FeaturedTracksViewModel
import a.sboev.ru.playlistmaker.library.ui.BindingFragment
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.search.ui.adapters.TrackAdapter
import a.sboev.ru.playlistmaker.utils.debounce
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment: BindingFragment<FragmentFeaturedTracksBinding>() {

    private val viewModel by viewModel<FeaturedTracksViewModel>()
    private lateinit var onItemClickDebounce: (Track) -> Unit
    private lateinit var adapter: TrackAdapter
    private var onItemClicked = object : TrackAdapter.OnItemClickListener {

        override fun onLongClick(track: Track) {}

        override fun onClick(track: Track) {
            onItemClickDebounce(track)
        }
    }
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFeaturedTracksBinding {
        return FragmentFeaturedTracksBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onItemClickDebounce = debounce(DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { track ->
            val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
            intent.putExtra(AudioPlayerActivity.BUNDLE_KEY, track)
            this.startActivity(intent)
        }
        binding.featuredTracksRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = TrackAdapter(onItemClicked)
        binding.featuredTracksRv.adapter = adapter
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is LibState.Loading -> showLoading()
                is LibState.Empty -> showErrorMessage()
                is LibState.Content<*> -> showContent(state.list)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun showLoading() {
        binding.apply {
            progressBarFeatured.isVisible = true
            placeHolder.isVisible = false
            featuredTracksRv.isVisible = false
        }
    }

    private fun showErrorMessage() {
        binding.apply {
            progressBarFeatured.isVisible = false
            placeHolder.isVisible = true
            featuredTracksRv.isVisible = false
            adapter.tracks = mutableListOf()
            adapter.refresh()
        }
    }

    private fun showContent(tracks: List<*>) {
        binding.apply {
            featuredTracksRv.isVisible = true
            progressBarFeatured.isVisible = false
            placeHolder.isVisible = false
            adapter.tracks = tracks as MutableList<Track>
            adapter.refresh()
        }
    }

    companion object {
        fun newInstance() = FeaturedTracksFragment()
        private const val DEBOUNCE_DELAY = 300L
    }


}