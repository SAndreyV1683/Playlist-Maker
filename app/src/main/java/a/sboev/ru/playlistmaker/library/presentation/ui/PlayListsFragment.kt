package a.sboev.ru.playlistmaker.library.presentation.ui


import a.sboev.ru.playlistmaker.databinding.FragmentPlayListsBinding
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.library.presentation.PlayListsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment: BindingFragment<FragmentPlayListsBinding>() {

    private val viewModel by viewModel<PlayListsViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayListsBinding {
        return FragmentPlayListsBinding.inflate(inflater, container, false)
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
            playlistsErrorImageView.isVisible = true
            playlistsErrorTextView.isVisible = true
        }
    }

    private fun showContent() {
        binding.apply {
            playlistsErrorImageView.isVisible = false
            playlistsErrorTextView.isVisible = false
        }
    }

    companion object {
        fun newInstance() = PlayListsFragment()
    }

}