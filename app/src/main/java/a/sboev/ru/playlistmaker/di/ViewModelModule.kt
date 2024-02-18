package a.sboev.ru.playlistmaker.di

import a.sboev.ru.playlistmaker.audioplayer.presentation.AudioPlayerViewModel
import a.sboev.ru.playlistmaker.search.presentation.SearchViewModel
import a.sboev.ru.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { params ->
        AudioPlayerViewModel(params.get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingsViewModel()
    }

}