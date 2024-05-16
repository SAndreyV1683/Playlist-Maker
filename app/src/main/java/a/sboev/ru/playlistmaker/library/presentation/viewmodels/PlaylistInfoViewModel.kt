package a.sboev.ru.playlistmaker.library.presentation.viewmodels

import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.library.ui.playlistinfo.models.PlaylistInfo
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewModel(
    private val playlistId: Long,
    private val playlistDatabaseInteractor: PlaylistDatabaseInteractor
): ViewModel() {

    private val infoState = MutableLiveData<PlaylistInfo>(null)
    fun observeInfoState(): LiveData<PlaylistInfo> = infoState
    private val tracksState = MutableLiveData<LibState>(LibState.Empty)
    fun observeTracksState(): LiveData<LibState> = tracksState

    fun getPlaylistTracks() {
        viewModelScope.launch {
            val playlist = withContext(Dispatchers.IO) {
                playlistDatabaseInteractor.getPlaylistById(playlistId)
            }
            val tracksIdList = playlist.tracksIdList
            playlistDatabaseInteractor.getPlaylistTracks(tracksIdList).collect { tracksList ->
                Log.d("PlaylistInfoViewModel", "tracks $tracksList")
                val duration = tracksList.sumOf { it.trackTimeMillis }
                infoState.value = PlaylistInfo(
                    playlist.name,
                    playlist.description,
                    playlist.uri,
                    SimpleDateFormat("mm", Locale.getDefault()).format(duration).toString(),
                    playlist.tracksCount
                )
            }
        }
    }



    private fun processTracks(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            tracksState.value = LibState.Empty
        } else {
            tracksState.value = LibState.Content(tracks)
        }
    }

}