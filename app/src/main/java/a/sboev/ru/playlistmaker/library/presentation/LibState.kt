package a.sboev.ru.playlistmaker.library.presentation

sealed interface LibState {

    data object Loading: LibState
    data class Content<T>(
        val list: List<T>
    ) : LibState
    data object Empty : LibState

}
