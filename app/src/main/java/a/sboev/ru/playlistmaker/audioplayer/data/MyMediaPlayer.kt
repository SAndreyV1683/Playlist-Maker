package a.sboev.ru.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import a.sboev.ru.playlistmaker.audioplayer.data.models.TrackUrlDto
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import a.sboev.ru.playlistmaker.audioplayer.presentation.PlayerState

class MyMediaPlayer(
    private val stateListener: PlayerRepository.StateListener
): Player {

    private val mediaPlayer = MediaPlayer()
    private var state: PlayerState = PlayerState.Default
    override fun preparePlayer(trackUrl: TrackUrlDto) {
        mediaPlayer.setDataSource(trackUrl.url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state = PlayerState.Prepared
            stateListener.state(state)
        }
        mediaPlayer.setOnCompletionListener {
            state = PlayerState.Prepared
            stateListener.state(state)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        state = PlayerState.Playing
        stateListener.state(state)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        state = PlayerState.Paused
        stateListener.state(state)
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getPosition(): Int = mediaPlayer.currentPosition

}