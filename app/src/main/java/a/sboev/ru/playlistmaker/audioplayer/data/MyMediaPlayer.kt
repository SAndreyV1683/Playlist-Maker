package a.sboev.ru.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import a.sboev.ru.playlistmaker.audioplayer.data.models.TrackUrlDto
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository

class MyMediaPlayer(
    private val stateListener: PlayerRepository.StateListener
): Player {

    private val mediaPlayer = MediaPlayer()
    private var state = STATE_DEFAULT
    override fun preparePlayer(trackUrl: TrackUrlDto) {
        mediaPlayer.setDataSource(trackUrl.url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state = STATE_PREPARED
            stateListener.state(state)
        }
        mediaPlayer.setOnCompletionListener {
            state = STATE_PREPARED
            stateListener.state(state)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        state = STATE_PLAYING
        stateListener.state(state)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        state = STATE_PAUSED
        stateListener.state(state)
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getPosition(): Int = mediaPlayer.currentPosition


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}