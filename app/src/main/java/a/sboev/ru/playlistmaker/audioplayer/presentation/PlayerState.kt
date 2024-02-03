package a.sboev.ru.playlistmaker.audioplayer.presentation

sealed class PlayerState {
    data object Prepared : PlayerState()
    data object Playing : PlayerState()
    data object Paused : PlayerState()
    data object Default : PlayerState()

}