package a.sboev.ru.playlistmaker.audioplayer.presentation

import a.sboev.ru.playlistmaker.audioplayer.data.MyMediaPlayer
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import a.sboev.ru.playlistmaker.audioplayer.ui.AudioPlayerActivity
import a.sboev.ru.playlistmaker.creators.PlayerInteractorCreator
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class AudioPlayerViewModel(
    track: Track?,
    application: Application
) : AndroidViewModel(application) {

    private var playerState = MutableLiveData<PlayerState>(PlayerState.Default)
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
    private val playerInteractor = PlayerInteractorCreator.providePlayerInteractor(MyMediaPlayer(stateListener))
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
        fun getViewModelProviderFactory(track: Track?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(
                    track,
                    this[APPLICATION_KEY] as Application
                )
            }
        }
    }

    override fun onCleared() {
        playerInteractor.release()
        super.onCleared()
    }
}