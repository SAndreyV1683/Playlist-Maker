package a.sboev.ru.playlistmaker.audioplayer.presentation

sealed class PlayerStateUi {
    data object Prepared : PlayerStateUi()
    data object Playing : PlayerStateUi()
    data object Paused : PlayerStateUi()
    data object Default : PlayerStateUi()

}