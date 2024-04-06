package a.sboev.ru.playlistmaker.audioplayer.data.models

sealed class PlayerStateDto(val progress: Int) {

    class Default : PlayerStateDto(0)

    class Prepared : PlayerStateDto(0)

    class Playing(progress: Int) : PlayerStateDto(progress)

    class Paused(progress: Int) : PlayerStateDto(progress)
}