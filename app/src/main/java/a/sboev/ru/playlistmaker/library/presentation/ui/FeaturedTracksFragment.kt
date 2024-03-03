package a.sboev.ru.playlistmaker.library.presentation.ui

import a.sboev.ru.playlistmaker.databinding.FragmentFeaturedTracksBinding
import a.sboev.ru.playlistmaker.library.presentation.FeaturedTracksViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturedTracksFragment: BindingFragment<FragmentFeaturedTracksBinding>() {

    val viewModel by viewModel<FeaturedTracksViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFeaturedTracksBinding {
        return FragmentFeaturedTracksBinding.inflate(inflater, container, false)
    }

}