package a.sboev.ru.playlistmaker.audioplayer.presentation

import a.sboev.ru.playlistmaker.audioplayer.data.MyMediaPlayer
import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerRepository
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import a.sboev.ru.playlistmaker.creators.PlayerInteractorCreator
import a.sboev.ru.playlistmaker.models.Track
import android.app.Application
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
    fun observePlayerState(): LiveData<PlayerState> = playerState
    private val stateListener = object : PlayerRepository.StateListener {
        override fun state(state: PlayerState) {
            playerState.postValue(state)
        }
    }
    private val playerInteractor = PlayerInteractorCreator.providePlayerInteractor(MyMediaPlayer(stateListener))
    init {
        preparePlayer(track)
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
            }
            else -> {}
        }
    }
    fun getTrackCurrentPosition(): String  = playerInteractor.getTrackCurrentPosition()

    companion object {
        val TAG = AudioPlayerViewModel::class.simpleName
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