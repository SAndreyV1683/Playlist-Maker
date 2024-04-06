package a.sboev.ru.playlistmaker.audioplayer.presentation

import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerInterActor
import a.sboev.ru.playlistmaker.audioplayer.domain.models.PlayerState
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class AudioPlayerViewModel(
    track: Track?,
) : ViewModel() {

    private val playerStateUi = MutableLiveData<PlayerStateUi>(PlayerStateUi.Default)
    private var trackPosition = MutableLiveData("0:00")
    fun observeTrackPosition(): LiveData<String> = trackPosition
    fun observePlayerState(): LiveData<PlayerStateUi> = playerStateUi

    private val playerInteractor: PlayerInterActor by inject(PlayerInterActor::class.java)

    init {
        viewModelScope.launch {
            playerInteractor.getPlayerState().collect { state ->
                val playerState = when(state) {
                    is PlayerState.Default -> PlayerStateUi.Default
                    is PlayerState.Prepared -> PlayerStateUi.Prepared
                    is PlayerState.Playing -> {
                        trackPosition.value = state.progress
                        PlayerStateUi.Playing
                    }
                    is PlayerState.Paused -> {
                        trackPosition.value = state.progress
                        PlayerStateUi.Paused
                    }
                }
                playerStateUi.postValue(playerState)
            }
        }
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
        when(playerStateUi.value) {
            PlayerStateUi.Playing -> {
                playerInteractor.pause()
                Log.d(TAG, "Pause")
            }
            PlayerStateUi.Prepared, PlayerStateUi.Paused -> {
                Log.d(TAG, "Play")
                playerInteractor.play()
            }
            else -> {}
        }
    }

    companion object {
        val TAG = AudioPlayerViewModel::class.simpleName
    }

    override fun onCleared() {
        playerInteractor.release()
        super.onCleared()
    }
}