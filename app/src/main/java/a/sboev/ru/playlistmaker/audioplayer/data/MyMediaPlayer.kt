package a.sboev.ru.playlistmaker.audioplayer.data

import a.sboev.ru.playlistmaker.audioplayer.data.models.PlayerStateDto
import a.sboev.ru.playlistmaker.audioplayer.data.models.TrackUrlDto
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyMediaPlayer: Player {

    private val mediaPlayer = MediaPlayer()
    private var state: MutableStateFlow<PlayerStateDto> = MutableStateFlow(PlayerStateDto.Default())
    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null
    override fun preparePlayer(trackUrl: TrackUrlDto) {
        mediaPlayer.setDataSource(trackUrl.url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            state.value = PlayerStateDto.Prepared()
        }
        mediaPlayer.setOnCompletionListener {
            state.value = PlayerStateDto.Prepared()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        job = scope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                if (mediaPlayer.isPlaying)
                    state.value = PlayerStateDto.Playing(mediaPlayer.currentPosition)
            }
        }
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        state.value = PlayerStateDto.Paused(mediaPlayer.currentPosition)
    }

    override fun release() {
        mediaPlayer.release()
        state.value = PlayerStateDto.Default()
        job?.cancel()
    }
    override fun getPlayerState(): Flow<PlayerStateDto> = state

}