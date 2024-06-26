package a.sboev.ru.playlistmaker.library.presentation.viewmodels

import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.library.ui.playlistinfo.models.PlaylistInfo
import a.sboev.ru.playlistmaker.search.domain.models.Track
import a.sboev.ru.playlistmaker.settings.domain.api.SharingInteractor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistInfoViewModel(
    private val playlistId: Long,
    private val playlistDatabaseInteractor: PlaylistDatabaseInteractor,
    private val sharingInteractor: SharingInteractor
): ViewModel() {

    private val playlistDeletionState = MutableLiveData<Boolean>(null)
    fun observeDeletionState(): LiveData<Boolean> = playlistDeletionState
    private val infoState = MutableLiveData<PlaylistInfo>(null)
    fun observeInfoState(): LiveData<PlaylistInfo> = infoState
    private val tracksState = MutableLiveData<LibState>(LibState.Empty)
    fun observeTracksState(): LiveData<LibState> = tracksState
    lateinit var playlist: Playlist
    lateinit var tracks: List<Track>

    fun getPlaylistTracks() {
        viewModelScope.launch {
            playlist = playlistDatabaseInteractor.getPlaylistById(playlistId)
            val tracksIdList = playlist.tracksIdList
            playlistDatabaseInteractor.getPlaylistTracks(tracksIdList).collect { tracksList ->
                val duration = tracksList.sumOf { it.trackTimeMillis }
                infoState.value = PlaylistInfo(
                    playlist.name,
                    playlist.description,
                    playlist.uri,
                    SimpleDateFormat("mm", Locale.getDefault()).format(duration).toString().toInt().toString(),
                    playlist.tracksCount
                )
                tracks = tracksList
                processTracks(tracksList)
            }
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistDatabaseInteractor.deletePlayListEntity(playlist).collect {
                playlistDeletionState.value = it
            }
        }
    }

    fun deleteTrackFromPlayList(track: Track) {
        viewModelScope.launch {
            playlistDatabaseInteractor.deleteTrackFromPlaylist(track, playlist)
            getPlaylistTracks()
        }
    }

    private fun processTracks(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            tracksState.value = LibState.Empty
        } else {
            tracksState.value = LibState.Content(tracks)
        }
    }

    fun sharePlaylist(shareString: String) {
        sharingInteractor.shareApp(shareString)
    }

}