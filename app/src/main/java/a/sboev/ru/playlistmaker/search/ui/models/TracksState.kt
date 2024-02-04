package a.sboev.ru.playlistmaker.search.ui.models

import a.sboev.ru.playlistmaker.search.domain.models.Track

sealed interface TracksState {
    data object Loading: TracksState
    data class Content(
        val tracks: List<Track>
    ): TracksState
    data class Error(
        val message: String
    ): TracksState
    data class Empty(
        val message: String
    ): TracksState
}