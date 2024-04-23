package a.sboev.ru.playlistmaker.library.presentation

import a.sboev.ru.playlistmaker.search.domain.models.Track

sealed interface LibState {

    data object Loading: LibState
    data class Content(
        val tracks: List<Track>
    ) : LibState
    data object Empty : LibState

}
