package a.sboev.ru.playlistmaker.di

import a.sboev.ru.playlistmaker.audioplayer.presentation.AudioPlayerViewModel
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.FeaturedTracksViewModel
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.NewPlaylistViewModel
import a.sboev.ru.playlistmaker.library.presentation.viewmodels.PlayListsViewModel
import a.sboev.ru.playlistmaker.search.presentation.SearchViewModel
import a.sboev.ru.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { params ->
        AudioPlayerViewModel(params.get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel()
    }

    viewModel {
        FeaturedTracksViewModel(get())
    }

    viewModel {
        PlayListsViewModel()
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }

}