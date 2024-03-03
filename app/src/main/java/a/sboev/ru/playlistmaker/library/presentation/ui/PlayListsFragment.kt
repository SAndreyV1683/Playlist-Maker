package a.sboev.ru.playlistmaker.library.presentation.ui


import a.sboev.ru.playlistmaker.databinding.FragmentPlayListsBinding
import a.sboev.ru.playlistmaker.library.presentation.FeaturedTracksViewModel
import a.sboev.ru.playlistmaker.library.presentation.PlayListsViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListsFragment: BindingFragment<FragmentPlayListsBinding>() {

    val viewModel by viewModel<PlayListsViewModel>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayListsBinding {
        return FragmentPlayListsBinding.inflate(inflater, container, false)
    }

}