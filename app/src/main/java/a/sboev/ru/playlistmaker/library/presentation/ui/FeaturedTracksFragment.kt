package a.sboev.ru.playlistmaker.library.presentation.ui

import a.sboev.ru.playlistmaker.databinding.FragmentFeaturedTracksBinding
import a.sboev.ru.playlistmaker.library.presentation.FeaturedTracksViewModel
import a.sboev.ru.playlistmaker.library.presentation.LibState
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment: BindingFragment<FragmentFeaturedTracksBinding>() {

    private val viewModel by viewModel<FeaturedTracksViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFeaturedTracksBinding {
        return FragmentFeaturedTracksBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is LibState.Error -> showErrorMessage()
                is LibState.Content -> showContent()
            }
        }
    }

    private fun showErrorMessage() {
        binding.apply {
            featuredTracksErrorTextView.isVisible = true
            featuredTracksErrorImageView.isVisible = true
        }
    }

    private fun showContent() {
        binding.apply {
            featuredTracksErrorTextView.isVisible = true
            featuredTracksErrorImageView.isVisible = true
        }
    }

    companion object {
        fun newInstance() = FeaturedTracksFragment()
    }

}