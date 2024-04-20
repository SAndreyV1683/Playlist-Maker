package a.sboev.ru.playlistmaker.library.ui.featuredtracks

import a.sboev.ru.playlistmaker.databinding.FragmentFeaturedTracksBinding
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.FeaturedTracksViewModel
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.library.ui.BindingFragment
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
        viewModel.fillData()
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is LibState.Loading -> showLoading()
                is LibState.Empty -> showErrorMessage()
                is LibState.Content -> showContent()
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            placeHolder.isVisible = false
        }
    }

    private fun showErrorMessage() {
        binding.apply {
            placeHolder.isVisible = true
        }
    }

    private fun showContent() {
        binding.apply {
            placeHolder.isVisible = false
        }
    }

    companion object {
        fun newInstance() = FeaturedTracksFragment()
    }

}