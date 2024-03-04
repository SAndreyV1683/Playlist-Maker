package a.sboev.ru.playlistmaker.library.presentation

sealed interface LibState {
    data object Content : LibState
    data object Error : LibState

}