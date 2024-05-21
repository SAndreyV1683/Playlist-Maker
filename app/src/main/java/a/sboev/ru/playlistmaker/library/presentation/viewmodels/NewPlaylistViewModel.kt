package a.sboev.ru.playlistmaker.library.presentation.viewmodels

import a.sboev.ru.playlistmaker.library.domain.api.FilesInteractor
import a.sboev.ru.playlistmaker.library.domain.api.PlaylistDatabaseInteractor
import a.sboev.ru.playlistmaker.library.domain.models.Playlist
import a.sboev.ru.playlistmaker.library.presentation.LibState
import a.sboev.ru.playlistmaker.search.domain.models.Track
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPlaylistViewModel(
    private val playlistDatabaseInteractor: PlaylistDatabaseInteractor,
    private val filesInteractor: FilesInteractor
): ViewModel() {

    private val playlistState = MutableLiveData<Playlist>(null)
    fun observePlaylistState(): LiveData<Playlist> = playlistState

    private val playlistUpdateState = MutableLiveData<Boolean>(null)
    fun observePlaylistUpdateState(): LiveData<Boolean> = playlistUpdateState
    private val playlistInsertState = MutableLiveData<Boolean>(null)
    fun observePlaylistInsertState(): LiveData<Boolean> = playlistInsertState


    fun createNewPlayList(
        playlistName: String,
        playlistDescription: String,
        playlistImageUri: String
    ) {
        viewModelScope.launch {
            val newFileUri = async {
                if (playlistImageUri.isNotEmpty()) {
                    filesInteractor.saveImage(Uri.parse(playlistImageUri), playlistName).toString()
                } else ""
            }
            playlistDatabaseInteractor.insertPlaylist(
                Playlist(
                    id = null,
                    name = playlistName,
                    description = playlistDescription,
                    uri = newFileUri.await(),
                    tracksIdList = emptyList(),
                    tracksCount = 0
                )
            ).collect {
                playlistInsertState.value = it
            }
        }
    }

    fun getPlaylistById(id: Long) {
        viewModelScope.launch {
            val playlist = playlistDatabaseInteractor.getPlaylistById(id)
            playlistState.value = playlist
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistDatabaseInteractor.updatePlayListEntity(playlist).collect {
                playlistUpdateState.value = it
            }
        }
    }

}