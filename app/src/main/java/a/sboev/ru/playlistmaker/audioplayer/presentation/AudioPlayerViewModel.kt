package a.sboev.ru.playlistmaker.audioplayer.presentation

import a.sboev.ru.playlistmaker.audioplayer.domain.api.PlayerInterActor
import a.sboev.ru.playlistmaker.audioplayer.domain.models.PlayerState
import a.sboev.ru.playlistmaker.audioplayer.domain.models.TrackUrl
import a.sboev.ru.playlistmaker.library.domain.api.DatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class AudioPlayerViewModel(
    private val track: Track?,
    private val databaseInteractor: DatabaseInteractor,
    private val playlistDatabaseInteractor: PlaylistDatabaseInteractor
) : ViewModel() {

    private val playerStateUi = MutableLiveData<PlayerStateUi>(PlayerStateUi.Default)
    private var trackPosition = MutableLiveData("0:00")
    private val favButtonSelectState = MutableLiveData(false)
    fun observeTrackPosition(): LiveData<String> = trackPosition
    fun observePlayerState(): LiveData<PlayerStateUi> = playerStateUi
    fun observeFavButtonState(): LiveData<Boolean> = favButtonSelectState

    private val playerInteractor: PlayerInterActor by inject(PlayerInterActor::class.java)

    private val stateLiveData = MutableLiveData<LibState>(LibState.Empty)
    fun observeState(): LiveData<LibState> = stateLiveData

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

    fun updatePlaylists() {
        viewModelScope.launch {
            playlistDatabaseInteractor.getPlaylists().collect { list ->
                processResult(list)
            }
        }
    }

    fun insertTrackToPlaylist(playlistId:Long, track: Track) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistDatabaseInteractor.insertTrackToPlayList(playlistId, track)
                playlistDatabaseInteractor.getPlaylists().collect { list ->
                    withContext(Dispatchers.Main) {
                        processResult(list)
                    }
                }
            }
        }
    }

    fun addTrackToFavorites(track: Track) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if(favButtonSelectState.value == false) {
                    track.timeToAddToFavorites = System.currentTimeMillis()
                    databaseInteractor.insertTrack(track)
                } else {
                    databaseInteractor.deleteTrack(track)
                }
                checkTackIsOnFavorites()
            }
        }
    }

    fun checkTackIsOnFavorites() {
        viewModelScope.launch {
            databaseInteractor.getTracksIdList().collect { list ->
                favButtonSelectState.value = list.contains(track?.trackId)
            }
        }
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

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            stateLiveData.value = LibState.Empty
        } else {
            stateLiveData.value = LibState.Content(playlists)
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