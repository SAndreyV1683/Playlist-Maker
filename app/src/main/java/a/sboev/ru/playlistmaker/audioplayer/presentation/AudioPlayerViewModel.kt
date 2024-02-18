package a.sboev.ru.playlistmaker.audioplayer.presentation

import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerInterActor
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class AudioPlayerViewModel(
    track: Track?,
) : ViewModel() {

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    private var trackPosition = MutableLiveData("0:00")
    fun observeTrackPosition(): LiveData<String> = trackPosition
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private val stateListener = object : PlayerRepository.StateListener {
        override fun state(state: Int) {
            val s = when(state) {
                0 -> PlayerState.Default
                1 -> PlayerState.Prepared
                2 -> PlayerState.Playing
                3 -> PlayerState.Paused
                else -> {PlayerState.Default}
            }
            playerState.postValue(s)
            currentPlayerState = s
        }
    }
    private val playerInteractor: PlayerInterActor by inject(PlayerInterActor::class.java) {
        parametersOf(stateListener)
    }
    init {
        preparePlayer(track)
    }
    private val handler = Handler(Looper.getMainLooper())
    private var currentPlayerState: PlayerState = PlayerState.Default
    private val playbackRunnable = object : Runnable {
        override fun run() {
            if (currentPlayerState is PlayerState.Playing) {
                trackPosition.value = playerInteractor.getTrackCurrentPosition()
                handler.postDelayed(this, TIMER_DELAY)
            } else {
                handler.removeCallbacks(this)
            }
        }
    }

    fun removeHandlerCallback() {
        handler.removeCallbacks(playbackRunnable)
    }

    private fun preparePlayer(track: Track?) {
        val previewUrl = track?.previewUrl
        if (previewUrl != null) {
            playerInteractor.prepare(TrackUrl(previewUrl))
        }
    }

    fun releasePlayer() {
        playerInteractor.release()
    }

    fun pausePlayer() {
        playerInteractor.pause()
    }

    fun playbackControl() {
        when(playerState.value) {
            PlayerState.Playing -> {
                playerInteractor.pause()
                Log.d(TAG, "Pause")
            }
            PlayerState.Prepared, PlayerState.Paused -> {
                Log.d(TAG, "Play")
                playerInteractor.play()
                handler.postDelayed(playbackRunnable, TIMER_DELAY)
            }
            else -> {}
        }
    }

    companion object {
        val TAG = AudioPlayerViewModel::class.simpleName
        private const val TIMER_DELAY = 500L
    }

    override fun onCleared() {
        playerInteractor.release()
        super.onCleared()
    }
}